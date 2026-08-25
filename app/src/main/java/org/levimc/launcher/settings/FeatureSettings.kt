package org.levimc.launcher.settings

import android.content.Context
import com.google.gson.annotations.SerializedName

class FeatureSettings {
    var isVersionIsolationEnabled: Boolean = false
        set(value) {
            field = value
            autoSave()
        }

    var isLauncherManagedMcLoginEnabled: Boolean = false
        set(value) {
            field = value
            autoSave()
        }

    var isLogcatOverlayEnabled: Boolean = false
        set(value) {
            field = value
            autoSave()
        }

    private var crashUploadEnabled: Boolean? = true

    var isCrashUploadEnabled: Boolean
        get() = crashUploadEnabled ?: true
        set(value) {
            crashUploadEnabled = value
            autoSave()
        }

    enum class StorageType {
        INTERNAL,
        EXTERNAL,
        VERSION_ISOLATION,
        VERSION_ISOLATION_INTERNAL,
        VERSION_ISOLATION_EXTERNAL
    }

    private fun autoSave() {
        appContext?.let { SettingsStorage.save(it, this) }
    }

    companion object {
        @Volatile
        private var INSTANCE: FeatureSettings? = null
        private var appContext: Context? = null

        @JvmStatic
        fun init(context: Context) {
            appContext = context.applicationContext
        }

        @JvmStatic
        fun getInstance(): FeatureSettings {
            return INSTANCE ?: synchronized(this) {
                val context = appContext
                val instance = if (context != null) {
                    SettingsStorage.load(context)
                } else {
                    FeatureSettings()
                }
                INSTANCE = instance
                instance
            }
        }
    }
}
