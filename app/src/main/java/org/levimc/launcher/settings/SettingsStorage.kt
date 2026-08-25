package org.levimc.launcher.settings

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

object SettingsStorage {
    private const val SP_NAME = "feature_settings"
    private const val KEY_SETTINGS_JSON = "settings_json"
    private val gson = Gson()

    @JvmStatic
    fun save(context: Context, settings: FeatureSettings) {
        val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val json = gson.toJson(settings)
        sp.edit().putString(KEY_SETTINGS_JSON, json).apply()
    }

    @JvmStatic
    fun load(context: Context): FeatureSettings {
        val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val json = sp.getString(KEY_SETTINGS_JSON, null) ?: return FeatureSettings()
        return try {
            gson.fromJson(json, FeatureSettings::class.java) ?: FeatureSettings()
        } catch (e: JsonSyntaxException) {
            FeatureSettings()
        }
    }

    @JvmStatic
    fun clear(context: Context) {
        val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        sp.edit().remove(KEY_SETTINGS_JSON).apply()
    }
}
