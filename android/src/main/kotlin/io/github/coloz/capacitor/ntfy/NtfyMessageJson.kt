package io.github.coloz.capacitor.ntfy

import org.json.JSONObject

internal object NtfyMessageJson {
    fun fromRaw(raw: JSONObject): JSONObject = JSONObject().apply {
        put("id", raw.optString("id"))
        put("time", raw.optLong("time", System.currentTimeMillis() / 1000))
        put("event", raw.optString("event", "message"))
        put("topic", raw.optString("topic"))
        copyIfPresent(raw, this, "message")
        copyIfPresent(raw, this, "title")
        copyIfPresent(raw, this, "priority")
        copyIfPresent(raw, this, "tags")
        copyIfPresent(raw, this, "click")
        copyIfPresent(raw, this, "expires")
        put("raw", JSONObject(raw.toString()))
    }

    private fun copyIfPresent(source: JSONObject, target: JSONObject, key: String) {
        if (source.has(key) && !source.isNull(key)) target.put(key, source.get(key))
    }
}
