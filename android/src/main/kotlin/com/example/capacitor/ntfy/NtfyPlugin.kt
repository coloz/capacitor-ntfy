package com.example.capacitor.ntfy

import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.PluginMethod

@CapacitorPlugin(name = "Ntfy")
class NtfyPlugin : Plugin() {

    private val implementation = Ntfy()

    @PluginMethod
    fun echo(call: PluginCall) {
        val value = call.getString("value") ?: ""

        val ret = JSObject().apply {
            put("value", implementation.echo(value))
        }
        call.resolve(ret)
    }
}
