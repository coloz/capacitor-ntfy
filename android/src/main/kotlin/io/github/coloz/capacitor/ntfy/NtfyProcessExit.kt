package io.github.coloz.capacitor.ntfy

import android.app.ActivityManager
import android.app.ApplicationExitInfo
import android.content.Context
import android.os.Build

internal object NtfyProcessExit {
    fun wasStoppedByUser(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return false
        return runCatching {
            val activityManager = context.getSystemService(ActivityManager::class.java)
            activityManager.getHistoricalProcessExitReasons(context.packageName, 0, EXIT_HISTORY_LIMIT)
                .firstOrNull { it.processName == context.packageName }
                ?.reason == ApplicationExitInfo.REASON_USER_REQUESTED
        }.getOrDefault(false)
    }

    private const val EXIT_HISTORY_LIMIT = 10
}
