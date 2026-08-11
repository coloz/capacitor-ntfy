package io.github.coloz.capacitor.ntfy

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NtfyServiceRuntimeTrackerTest {
    @Test
    fun startingStateExpiresButRunningStateDoesNot() {
        var now = 1_000L
        val tracker = NtfyServiceRuntimeTracker { now }

        tracker.markStarting()
        assertTrue(tracker.isExpectedActive())

        now += NtfyServiceRuntimeTracker.START_TIMEOUT_MILLIS + 1L
        assertFalse(tracker.isExpectedActive())

        tracker.markRunning()
        assertTrue(tracker.isExpectedActive())

        tracker.markStopped()
        assertFalse(tracker.isExpectedActive())
    }
}
