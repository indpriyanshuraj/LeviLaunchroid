package org.levimc.launcher.core.curseforge.models

import com.google.gson.annotations.SerializedName

class ModFilesResponse {
    @SerializedName("data")
    @JvmField
    var data: List<ContentFile>? = null
}
