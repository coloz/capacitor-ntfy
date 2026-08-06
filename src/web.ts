import { WebPlugin } from '@capacitor/core';

import type { NtfyPlugin } from './definitions';

export class NtfyWeb extends WebPlugin implements NtfyPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
