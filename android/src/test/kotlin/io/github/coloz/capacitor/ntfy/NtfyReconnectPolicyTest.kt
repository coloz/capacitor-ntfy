package io.github.coloz.capacitor.ntfy

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NtfyReconnectPolicyTest {
    @Test
    fun backoffIsCappedAndCanBeResetAfterAStableConnection() {
        val policy = NtfyReconnectPolicy()

        assertFalse(policy.isRetrying)
        assertEquals(listOf(2, 4, 8, 16, 32, 60, 60), List(7) { policy.nextDelaySeconds() })
        assertTrue(policy.isRetrying)

        policy.reset()

        assertFalse(policy.isRetrying)
        assertEquals(2, policy.nextDelaySeconds())
    }
}
