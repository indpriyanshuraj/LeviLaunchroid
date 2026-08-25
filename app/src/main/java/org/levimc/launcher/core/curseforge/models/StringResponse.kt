package org.levimc.launcher.core.curseforge.models

import com.google.gson.annotations.SerializedName

class StringResponse {
    @SerializedName("data")
    @JvmField
    var data: String? = null
}
