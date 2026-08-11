package io.github.coloz.capacitor.ntfy

import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NtfyNotificationProfileTest {
    @Test
    fun createsAStableChannelForEveryNtfyPriority() {
        val profiles = (1..5).map(::ntfyNotificationProfile)

        assertEquals(
            listOf("messages_min", "messages_low", "messages_default", "messages_high", "messages_urgent"),
            profiles.map { it.channelId("messages") },
        )
        assertEquals(
            listOf(
                NotificationManager.IMPORTANCE_MIN,
                NotificationManager.IMPORTANCE_LOW,
                NotificationManager.IMPORTANCE_DEFAULT,
                NotificationManager.IMPORTANCE_HIGH,
                NotificationManager.IMPORTANCE_HIGH,
            ),
            profiles.map { it.importance },
        )
        assertEquals(NotificationCompat.PRIORITY_MIN, profiles[0].compatPriority)
        assertEquals(NotificationCompat.PRIORITY_MAX, profiles[4].compatPriority)
        assertNull(profiles[0].vibrationPattern)
        assertArrayEquals(longArrayOf(0L, 1_000L, 500L, 1_000L, 500L, 1_000L), profiles[4].vibrationPattern)
    }
}
