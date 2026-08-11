package io.github.coloz.capacitor.ntfy

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class NtfyNetworkSnapshotTest {
    @Test
    fun changingDefaultNetworkIsDetectedEvenWhenBothNetworksAreAvailable() {
        val wifi = NtfyNetworkSnapshot(handle = 100L, available = true)
        val cellular = NtfyNetworkSnapshot(handle = 200L, available = true)

        assertNotEquals(wifi, cellular)
        assertEquals(wifi, wifi.copy())
    }
}
