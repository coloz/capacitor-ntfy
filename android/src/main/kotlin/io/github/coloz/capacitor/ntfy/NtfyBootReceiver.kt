package io.github.coloz.capacitor.ntfy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class NtfyBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED && intent.action != Intent.ACTION_MY_PACKAGE_REPLACED) return
        val store = NtfyStore(context)
        val config = store.loadConfig() ?: return
        if (!store.isEnabled() || !config.autoStartOnBoot) return
        runCatching {
            ContextCompat.startForegroundService(
                context,
                Intent(context, NtfyForegroundService::class.java).setAction(NtfyForegroundService.ACTION_START),
            )
        }
    }
}
