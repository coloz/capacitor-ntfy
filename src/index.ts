import { registerPlugin } from '@capacitor/core';

import type { NtfyPlugin } from './definitions';

const Ntfy = registerPlugin<NtfyPlugin>('Ntfy', {
  web: () => import('./web').then((m) => new m.NtfyWeb()),
});

export * from './definitions';
export { Ntfy };
