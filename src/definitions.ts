export interface NtfyPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
