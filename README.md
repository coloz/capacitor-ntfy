# capacitor-ntfy

Android-first Capacitor 8 plugin for receiving messages from a self-hosted or public ntfy server through a native foreground service. The connection continues when the WebView is suspended, received messages are retained locally, and native Android notifications can be shown immediately.

This project includes a complete demo application in [`demo/`](./demo/).

## Platform support

| Platform | Support |
| --- | --- |
| Android 7+ | Native foreground service, JSON stream, notifications, reboot recovery |
| Web | Foreground-only streaming implementation for development |
| iOS | Intentional stub; use APNs for production iOS push delivery |

The Android implementation does not use Firebase or a handset vendor push SDK. It therefore needs a visible foreground-service notification and cannot receive messages after the user force-stops the application.

## Install

```bash
npm install capacitor-ntfy
npx cap sync android
```

The plugin manifest contributes the Internet, notification, boot, and foreground-service permissions. Android 13+ notification permission still has to be requested at runtime.

## Quick start

```ts
import { Ntfy } from 'capacitor-ntfy';

await Ntfy.requestNotificationPermission();

await Ntfy.start({
  baseUrl: 'https://ntfy.example.com',
  topics: ['orders-alice'],
  token: 'tk_example',
  autoStartOnBoot: true,
  showNotifications: true,
});

await Ntfy.addListener('messageReceived', (message) => {
  console.log('ntfy message', message);
});

await Ntfy.addListener('statusChanged', (status) => {
  console.log('ntfy state', status.state);
});
```

The plugin stores the active configuration so Android can restart the service. Tokens and passwords are encrypted with an AES-GCM key held by Android Keystore. Message history is stored in private application preferences and is capped by `historyLimit`.

## Sending a message

Use your backend in production. The `publish` method is included for diagnostics and the demo:

```ts
await Ntfy.publish({
  baseUrl: 'https://ntfy.example.com',
  topic: 'orders-alice',
  title: 'New order',
  message: 'Order #1042 is ready.',
  token: 'tk_example',
});
```

Do not place a privileged server token in client code. Give each device narrowly scoped ntfy credentials or publish through your authenticated backend.

## Reliability notes

- Ask the user to set the app battery mode to unrestricted if instant delivery is a core feature. `openBatteryOptimizationSettings()` opens the standard Android list.
- Some Android variants also require the user to enable auto-start. This is outside the standard Android API and is not automated by this plugin.
- `START_STICKY` and a boot receiver help recover the service, but user force-stop always wins.
- The last received ntfy message ID is reused as `since` after reconnect, and message IDs are deduplicated locally.
- Use HTTPS. Plain HTTP may require a host application network-security configuration and is unsuitable for credentials.
- Android 14+ declares the foreground service as `specialUse`. App-store policy review may require explaining why continuous, user-visible notification delivery is the app's core function.

## Android customization

Notification channel IDs are created the first time the service starts and Android keeps user channel choices. Changing a channel's importance programmatically after creation has no effect; use a new channel ID when required.

The plugin uses the host application's launcher icon as its default notification small icon. A production application should replace this with a dedicated monochrome notification asset in a fork or expose an application-specific resource.

## Development

```bash
npm install
npm run build
npm run verify:android

cd demo
npm install
npm run build
npx cap sync android
npx cap open android
```

The Android plugin namespace is `io.github.coloz.capacitor.ntfy`, and the Demo application ID is `io.github.coloz.capacitor.ntfy.demo`.

## API

<docgen-index>

* [`start(...)`](#start)
* [`stop()`](#stop)
* [`getStatus()`](#getstatus)
* [`getMessages(...)`](#getmessages)
* [`clearMessages()`](#clearmessages)
* [`publish(...)`](#publish)
* [`requestNotificationPermission()`](#requestnotificationpermission)
* [`getNotificationPermission()`](#getnotificationpermission)
* [`isIgnoringBatteryOptimizations()`](#isignoringbatteryoptimizations)
* [`openBatteryOptimizationSettings()`](#openbatteryoptimizationsettings)
* [`openAppSettings()`](#openappsettings)
* [`addListener('messageReceived', ...)`](#addlistenermessagereceived-)
* [`addListener('statusChanged', ...)`](#addlistenerstatuschanged-)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### start(...)

```typescript
start(options: NtfyStartOptions) => Promise<NtfyStatus>
```

| Param         | Type                                                          |
| ------------- | ------------------------------------------------------------- |
| **`options`** | <code><a href="#ntfystartoptions">NtfyStartOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#ntfystatus">NtfyStatus</a>&gt;</code>

--------------------


### stop()

```typescript
stop() => Promise<NtfyStatus>
```

**Returns:** <code>Promise&lt;<a href="#ntfystatus">NtfyStatus</a>&gt;</code>

--------------------


### getStatus()

```typescript
getStatus() => Promise<NtfyStatus>
```

**Returns:** <code>Promise&lt;<a href="#ntfystatus">NtfyStatus</a>&gt;</code>

--------------------


### getMessages(...)

```typescript
getMessages(options?: { limit?: number | undefined; } | undefined) => Promise<{ messages: NtfyMessage[]; }>
```

| Param         | Type                             |
| ------------- | -------------------------------- |
| **`options`** | <code>{ limit?: number; }</code> |

**Returns:** <code>Promise&lt;{ messages: NtfyMessage[]; }&gt;</code>

--------------------


### clearMessages()

```typescript
clearMessages() => Promise<void>
```

--------------------


### publish(...)

```typescript
publish(options: NtfyPublishOptions) => Promise<NtfyMessage>
```

| Param         | Type                                                              |
| ------------- | ----------------------------------------------------------------- |
| **`options`** | <code><a href="#ntfypublishoptions">NtfyPublishOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#ntfymessage">NtfyMessage</a>&gt;</code>

--------------------


### requestNotificationPermission()

```typescript
requestNotificationPermission() => Promise<{ state: NtfyPermissionState; }>
```

**Returns:** <code>Promise&lt;{ state: <a href="#ntfypermissionstate">NtfyPermissionState</a>; }&gt;</code>

--------------------


### getNotificationPermission()

```typescript
getNotificationPermission() => Promise<{ state: NtfyPermissionState; }>
```

**Returns:** <code>Promise&lt;{ state: <a href="#ntfypermissionstate">NtfyPermissionState</a>; }&gt;</code>

--------------------


### isIgnoringBatteryOptimizations()

```typescript
isIgnoringBatteryOptimizations() => Promise<{ value: boolean; }>
```

**Returns:** <code>Promise&lt;{ value: boolean; }&gt;</code>

--------------------


### openBatteryOptimizationSettings()

```typescript
openBatteryOptimizationSettings() => Promise<void>
```

--------------------


### openAppSettings()

```typescript
openAppSettings() => Promise<void>
```

--------------------


### addListener('messageReceived', ...)

```typescript
addListener(eventName: 'messageReceived', listenerFunc: (message: NtfyMessage) => void) => Promise<PluginListenerHandle>
```

| Param              | Type                                                                      |
| ------------------ | ------------------------------------------------------------------------- |
| **`eventName`**    | <code>'messageReceived'</code>                                            |
| **`listenerFunc`** | <code>(message: <a href="#ntfymessage">NtfyMessage</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('statusChanged', ...)

```typescript
addListener(eventName: 'statusChanged', listenerFunc: (status: NtfyStatus) => void) => Promise<PluginListenerHandle>
```

| Param              | Type                                                                   |
| ------------------ | ---------------------------------------------------------------------- |
| **`eventName`**    | <code>'statusChanged'</code>                                           |
| **`listenerFunc`** | <code>(status: <a href="#ntfystatus">NtfyStatus</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### removeAllListeners()

```typescript
removeAllListeners() => Promise<void>
```

--------------------


### Interfaces


#### NtfyStatus

| Prop                              | Type                                                                |
| --------------------------------- | ------------------------------------------------------------------- |
| **`state`**                       | <code><a href="#ntfyconnectionstate">NtfyConnectionState</a></code> |
| **`running`**                     | <code>boolean</code>                                                |
| **`connected`**                   | <code>boolean</code>                                                |
| **`baseUrl`**                     | <code>string</code>                                                 |
| **`topics`**                      | <code>string[]</code>                                               |
| **`lastMessageId`**               | <code>string</code>                                                 |
| **`lastError`**                   | <code>string</code>                                                 |
| **`retryInSeconds`**              | <code>number</code>                                                 |
| **`batteryOptimizationsIgnored`** | <code>boolean</code>                                                |
| **`notificationPermission`**      | <code><a href="#ntfypermissionstate">NtfyPermissionState</a></code> |


#### NtfyStartOptions

| Prop                     | Type                  | Description                                                                 |
| ------------------------ | --------------------- | --------------------------------------------------------------------------- |
| **`baseUrl`**            | <code>string</code>   | ntfy server URL, for example https://ntfy.sh. HTTPS is recommended.         |
| **`topics`**             | <code>string[]</code> | One or more ntfy topic names.                                               |
| **`token`**              | <code>string</code>   | ntfy access token. Takes precedence over username/password.                 |
| **`username`**           | <code>string</code>   |                                                                             |
| **`password`**           | <code>string</code>   |                                                                             |
| **`initialSince`**       | <code>string</code>   | Initial ntfy `since` value. Defaults to `10m`.                              |
| **`autoStartOnBoot`**    | <code>boolean</code>  | Restart the foreground service after a device reboot. Defaults to true.     |
| **`showNotifications`**  | <code>boolean</code>  | Show a local notification for each received ntfy message. Defaults to true. |
| **`historyLimit`**       | <code>number</code>   | Maximum number of messages retained by the plugin. Defaults to 100.         |
| **`foregroundTitle`**    | <code>string</code>   |                                                                             |
| **`foregroundText`**     | <code>string</code>   |                                                                             |
| **`serviceChannelId`**   | <code>string</code>   |                                                                             |
| **`serviceChannelName`** | <code>string</code>   |                                                                             |
| **`messageChannelId`**   | <code>string</code>   |                                                                             |
| **`messageChannelName`** | <code>string</code>   |                                                                             |


#### NtfyMessage

| Prop           | Type                                                             | Description                 |
| -------------- | ---------------------------------------------------------------- | --------------------------- |
| **`id`**       | <code>string</code>                                              |                             |
| **`time`**     | <code>number</code>                                              |                             |
| **`event`**    | <code>string</code>                                              |                             |
| **`topic`**    | <code>string</code>                                              |                             |
| **`message`**  | <code>string</code>                                              |                             |
| **`title`**    | <code>string</code>                                              |                             |
| **`priority`** | <code>number</code>                                              |                             |
| **`tags`**     | <code>string[]</code>                                            |                             |
| **`click`**    | <code>string</code>                                              |                             |
| **`expires`**  | <code>number</code>                                              |                             |
| **`raw`**      | <code><a href="#record">Record</a>&lt;string, unknown&gt;</code> | Original ntfy JSON payload. |


#### NtfyPublishOptions

| Prop           | Type                  |
| -------------- | --------------------- |
| **`baseUrl`**  | <code>string</code>   |
| **`topic`**    | <code>string</code>   |
| **`message`**  | <code>string</code>   |
| **`title`**    | <code>string</code>   |
| **`priority`** | <code>number</code>   |
| **`tags`**     | <code>string[]</code> |
| **`click`**    | <code>string</code>   |
| **`token`**    | <code>string</code>   |
| **`username`** | <code>string</code>   |
| **`password`** | <code>string</code>   |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


### Type Aliases


#### NtfyConnectionState

<code>'stopped' | 'starting' | 'connecting' | 'connected' | 'waitingForNetwork' | 'reconnecting' | 'error'</code>


#### NtfyPermissionState

<code>'granted' | 'denied' | 'prompt' | 'unsupported'</code>


#### Record

Construct a type with a set of properties K of type T

<code>{ [P in K]: T; }</code>

</docgen-api>

## License

MIT
