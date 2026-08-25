package org.levimc.launcher.core.curseforge.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ContentLinks : Serializable {
    @SerializedName("websiteUrl")
    @JvmField
    var websiteUrl: String? = null
}
