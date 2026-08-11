package io.github.coloz.capacitor.ntfy

import kotlin.math.min

internal class NtfyReconnectPolicy {
    private var failures = 0

    val isRetrying: Boolean
        get() = failures > 0

    fun reset() {
        failures = 0
    }

    fun nextDelaySeconds(): Int {
        failures += 1
        return min(MAX_DELAY_SECONDS, 1 shl min(failures, MAX_EXPONENT))
    }

    companion object {
        private const val MAX_DELAY_SECONDS = 60
        private const val MAX_EXPONENT = 6
    }
}
