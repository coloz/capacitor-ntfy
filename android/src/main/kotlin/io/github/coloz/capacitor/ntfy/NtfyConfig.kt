package io.github.coloz.capacitor.ntfy

import org.json.JSONArray
import org.json.JSONObject

internal data class NtfyConfig(
    val baseUrl: String,
    val topics: List<String>,
    val token: String?,
    val username: String?,
    val password: String?,
    val initialSince: String,
    val autoStartOnBoot: Boolean,
    val showNotifications: Boolean,
    val historyLimit: Int,
    val foregroundTitle: String,
    val foregroundText: String,
    val serviceChannelId: String,
    val serviceChannelName: String,
    val messageChannelId: String,
    val messageChannelName: String,
) {
    val signature: String
        get() = "$baseUrl|${topics.joinToString(",")}"

    fun toJson(): JSONObject = JSONObject().apply {
        put("baseUrl", baseUrl)
        put("topics", JSONArray(topics))
        put("token", token ?: JSONObject.NULL)
        put("username", username ?: JSONObject.NULL)
        put("password", password ?: JSONObject.NULL)
        put("initialSince", initialSince)
        put("autoStartOnBoot", autoStartOnBoot)
        put("showNotifications", showNotifications)
        put("historyLimit", historyLimit)
        put("foregroundTitle", foregroundTitle)
        put("foregroundText", foregroundText)
        put("serviceChannelId", serviceChannelId)
        put("serviceChannelName", serviceChannelName)
        put("messageChannelId", messageChannelId)
        put("messageChannelName", messageChannelName)
    }

    companion object {
        fun fromJson(json: JSONObject): NtfyConfig {
            val topicsJson = json.getJSONArray("topics")
            val topics = buildList {
                for (index in 0 until topicsJson.length()) add(topicsJson.getString(index))
            }
            return NtfyConfig(
                baseUrl = json.getString("baseUrl"),
                topics = topics,
                token = json.optStringOrNull("token"),
                username = json.optStringOrNull("username"),
                password = json.optStringOrNull("password"),
                initialSince = json.optString("initialSince", "10m"),
                autoStartOnBoot = json.optBoolean("autoStartOnBoot", true),
                showNotifications = json.optBoolean("showNotifications", true),
                historyLimit = json.optInt("historyLimit", 100).coerceIn(1, 500),
                foregroundTitle = json.optString("foregroundTitle", "ntfy 实时连接"),
                foregroundText = json.optString("foregroundText", "正在等待新消息"),
                serviceChannelId = json.optString("serviceChannelId", "capacitor_ntfy_service"),
                serviceChannelName = json.optString("serviceChannelName", "ntfy 后台连接"),
                messageChannelId = json.optString("messageChannelId", "capacitor_ntfy_messages"),
                messageChannelName = json.optString("messageChannelName", "ntfy 消息"),
            )
        }
    }
}

private fun JSONObject.optStringOrNull(key: String): String? {
    if (!has(key) || isNull(key)) return null
    return optString(key).takeIf { it.isNotBlank() }
}
