package io.github.coloz.capacitor.ntfy

import java.util.concurrent.CopyOnWriteArraySet
import org.json.JSONObject

internal interface NtfyEventListener {
    fun onStatusChanged(status: JSONObject)
    fun onMessageReceived(message: JSONObject)
}

internal object NtfyEventBus {
    private val listeners = CopyOnWriteArraySet<NtfyEventListener>()

    fun add(listener: NtfyEventListener) = listeners.add(listener)

    fun remove(listener: NtfyEventListener) = listeners.remove(listener)

    fun statusChanged(status: JSONObject) {
        listeners.forEach { it.onStatusChanged(JSONObject(status.toString())) }
    }

    fun messageReceived(message: JSONObject) {
        listeners.forEach { it.onMessageReceived(JSONObject(message.toString())) }
    }
}
