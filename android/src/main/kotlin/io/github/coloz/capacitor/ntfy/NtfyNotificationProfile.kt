package io.github.coloz.capacitor.ntfy

import android.app.NotificationManager
import androidx.core.app.NotificationCompat

internal data class NtfyNotificationProfile(
    val priority: Int,
    val channelSuffix: String,
    val channelNameSuffix: String,
    val importance: Int,
    val compatPriority: Int,
    val vibrationPattern: LongArray?,
) {
    fun channelId(baseId: String): String = "${baseId}_$channelSuffix"
}

internal fun ntfyNotificationProfile(value: Int): NtfyNotificationProfile = when (value.coerceIn(1, 5)) {
    1 -> NtfyNotificationProfile(
        priority = 1,
        channelSuffix = "min",
        channelNameSuffix = "最低",
        importance = NotificationManager.IMPORTANCE_MIN,
        compatPriority = NotificationCompat.PRIORITY_MIN,
        vibrationPattern = null,
    )
    2 -> NtfyNotificationProfile(
        priority = 2,
        channelSuffix = "low",
        channelNameSuffix = "低",
        importance = NotificationManager.IMPORTANCE_LOW,
        compatPriority = NotificationCompat.PRIORITY_LOW,
        vibrationPattern = null,
    )
    4 -> NtfyNotificationProfile(
        priority = 4,
        channelSuffix = "high",
        channelNameSuffix = "高",
        importance = NotificationManager.IMPORTANCE_HIGH,
        compatPriority = NotificationCompat.PRIORITY_HIGH,
        vibrationPattern = longArrayOf(0L, 500L, 250L, 500L),
    )
    5 -> NtfyNotificationProfile(
        priority = 5,
        channelSuffix = "urgent",
        channelNameSuffix = "紧急",
        importance = NotificationManager.IMPORTANCE_HIGH,
        compatPriority = NotificationCompat.PRIORITY_MAX,
        vibrationPattern = longArrayOf(0L, 1_000L, 500L, 1_000L, 500L, 1_000L),
    )
    else -> NtfyNotificationProfile(
        priority = 3,
        channelSuffix = "default",
        channelNameSuffix = "默认",
        importance = NotificationManager.IMPORTANCE_DEFAULT,
        compatPriority = NotificationCompat.PRIORITY_DEFAULT,
        vibrationPattern = longArrayOf(0L, 250L),
    )
}
