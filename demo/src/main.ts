import { Capacitor } from '@capacitor/core';
import { Ntfy, type NtfyMessage, type NtfyStatus } from 'capacitor-ntfy';
import './style.css';

const savedTopic = localStorage.getItem('ntfy-demo-topic');
const randomTopicSuffix =
  typeof crypto.randomUUID === 'function' ? crypto.randomUUID().slice(0, 8) : Math.random().toString(36).slice(2, 10);
const defaultTopic = savedTopic ?? `capacitor-demo-${randomTopicSuffix}`;
localStorage.setItem('ntfy-demo-topic', defaultTopic);

const appElement = document.querySelector<HTMLDivElement>('#app');
if (!appElement) throw new Error('Missing #app element');

appElement.innerHTML = `
  <main>
    <header class="hero">
      <span class="eyebrow">CAPACITOR · ANDROID · NTFY</span>
      <h1>实时消息实验台</h1>
      <p>使用 Android 原生前台服务连接 ntfy。启动后把 App 切到后台，再发送测试消息。</p>
      <div class="platform">当前平台：<strong>${Capacitor.getPlatform()}</strong></div>
    </header>

    <section class="status-grid" aria-label="连接状态">
      <article><span>服务</span><strong id="state">读取中</strong></article>
      <article><span>连接</span><strong id="connected">—</strong></article>
      <article><span>通知权限</span><strong id="permission">—</strong></article>
      <article><span>电池限制</span><strong id="battery">—</strong></article>
    </section>

    <section class="panel">
      <div class="section-title"><div><span>01</span><h2>连接配置</h2></div></div>
      <label>ntfy 服务地址<input id="baseUrl" value="https://ntfy.sh" inputmode="url" /></label>
      <label>Topic<input id="topic" value="${defaultTopic}" autocomplete="off" /></label>
      <div class="split">
        <label>Access token（可选）<input id="token" type="password" autocomplete="off" /></label>
        <label>初次补拉范围<input id="since" value="10m" /></label>
      </div>
      <label class="check"><input id="showNotifications" type="checkbox" checked />收到消息时显示系统通知</label>
      <div class="actions">
        <button id="permissionButton" class="secondary">授权通知</button>
        <button id="startButton">启动实时连接</button>
        <button id="stopButton" class="danger">停止</button>
      </div>
      <div class="actions compact">
        <button id="batteryButton" class="ghost">打开电池优化设置</button>
        <button id="appSettingsButton" class="ghost">打开应用设置</button>
      </div>
    </section>

    <section class="panel">
      <div class="section-title"><div><span>02</span><h2>发送测试消息</h2></div></div>
      <label>标题<input id="testTitle" value="Capacitor ntfy 测试" /></label>
      <label>内容<textarea id="testMessage" rows="3">这条消息由 Demo App 通过 ntfy 发布。</textarea></label>
      <button id="publishButton">发送到当前 Topic</button>
      <p class="hint">生产环境应由你的业务服务器发布，Demo 中的 publish 仅用于联调。</p>
    </section>

    <section class="panel messages-panel">
      <div class="section-title">
        <div><span>03</span><h2>最近消息</h2></div>
        <button id="clearButton" class="text-button">清空</button>
      </div>
      <div id="messages" class="messages"><p class="empty">还没有收到消息</p></div>
    </section>

    <div id="toast" role="status" aria-live="polite"></div>
  </main>
`;

const byId = <T extends HTMLElement>(id: string): T => {
  const element = document.querySelector<T>(`#${id}`);
  if (!element) throw new Error(`Missing #${id} element`);
  return element;
};
const baseUrlInput = byId<HTMLInputElement>('baseUrl');
const topicInput = byId<HTMLInputElement>('topic');
const tokenInput = byId<HTMLInputElement>('token');
const sinceInput = byId<HTMLInputElement>('since');
const showNotificationsInput = byId<HTMLInputElement>('showNotifications');
const messagesElement = byId<HTMLDivElement>('messages');
const toastElement = byId<HTMLDivElement>('toast');
let messages: NtfyMessage[] = [];
let toastTimer: number | undefined;

function toast(message: string, isError = false): void {
  toastElement.textContent = message;
  toastElement.className = isError ? 'visible error' : 'visible';
  window.clearTimeout(toastTimer);
  toastTimer = window.setTimeout(() => (toastElement.className = ''), 3200);
}

function renderStatus(status: NtfyStatus): void {
  byId('state').textContent = status.state;
  byId('connected').textContent = status.connected ? '已连接' : '未连接';
  byId('permission').textContent = status.notificationPermission;
  byId('battery').textContent = status.batteryOptimizationsIgnored ? '不受限制' : '可能受限';
  byId('state').className = status.connected ? 'good' : status.running ? 'waiting' : '';
}

function renderMessages(): void {
  messagesElement.replaceChildren();
  if (!messages.length) {
    const empty = document.createElement('p');
    empty.className = 'empty';
    empty.textContent = '还没有收到消息';
    messagesElement.append(empty);
    return;
  }
  for (const item of messages) {
    const card = document.createElement('article');
    card.className = 'message';
    const meta = document.createElement('div');
    meta.className = 'message-meta';
    const topic = document.createElement('span');
    topic.textContent = item.topic;
    const time = document.createElement('time');
    time.textContent = new Date(item.time * 1000).toLocaleString();
    meta.append(topic, time);
    const title = document.createElement('h3');
    title.textContent = item.title ?? '无标题消息';
    const body = document.createElement('p');
    body.textContent = item.message ?? '';
    const id = document.createElement('code');
    id.textContent = item.id;
    card.append(meta, title, body, id);
    messagesElement.append(card);
  }
}

function credentials(): { token?: string } {
  const token = tokenInput.value.trim();
  return token ? { token } : {};
}

async function refresh(): Promise<void> {
  const [status, history] = await Promise.all([Ntfy.getStatus(), Ntfy.getMessages({ limit: 50 })]);
  renderStatus(status);
  messages = history.messages;
  renderMessages();
}

byId('permissionButton').addEventListener('click', async () => {
  try {
    const result = await Ntfy.requestNotificationPermission();
    toast(`通知权限：${result.state}`);
    await refresh();
  } catch (error) {
    toast(String(error), true);
  }
});

byId('startButton').addEventListener('click', async () => {
  try {
    const topic = topicInput.value.trim();
    localStorage.setItem('ntfy-demo-topic', topic);
    const status = await Ntfy.start({
      baseUrl: baseUrlInput.value.trim(),
      topics: [topic],
      initialSince: sinceInput.value.trim() || '10m',
      showNotifications: showNotificationsInput.checked,
      autoStartOnBoot: true,
      foregroundTitle: 'ntfy Demo 实时连接',
      foregroundText: `正在监听 ${topic}`,
      ...credentials(),
    });
    renderStatus(status);
    toast('前台服务已启动');
  } catch (error) {
    toast(String(error), true);
  }
});

byId('stopButton').addEventListener('click', async () => {
  try {
    renderStatus(await Ntfy.stop());
    toast('实时连接已停止');
  } catch (error) {
    toast(String(error), true);
  }
});

byId('publishButton').addEventListener('click', async () => {
  try {
    await Ntfy.publish({
      baseUrl: baseUrlInput.value.trim(),
      topic: topicInput.value.trim(),
      title: byId<HTMLInputElement>('testTitle').value,
      message: byId<HTMLTextAreaElement>('testMessage').value,
      priority: 4,
      ...credentials(),
    });
    toast('测试消息已发布');
  } catch (error) {
    toast(String(error), true);
  }
});

byId('batteryButton').addEventListener('click', () => {
  void Ntfy.openBatteryOptimizationSettings().catch((error) => toast(String(error), true));
});

byId('appSettingsButton').addEventListener('click', () => {
  void Ntfy.openAppSettings().catch((error) => toast(String(error), true));
});

byId('clearButton').addEventListener('click', async () => {
  await Ntfy.clearMessages();
  messages = [];
  renderMessages();
});

void Ntfy.addListener('statusChanged', renderStatus);
void Ntfy.addListener('messageReceived', (message) => {
  messages = [message, ...messages.filter((item) => item.id !== message.id)].slice(0, 50);
  renderMessages();
});

void refresh().catch((error) => toast(String(error), true));
window.setInterval(() => void Ntfy.getStatus().then(renderStatus), 10_000);
