package org.levimc.launcher.core.curseforge.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ContentAsset : Serializable {
    @SerializedName("id")
    @JvmField
    var id: Int = 0

    @SerializedName("modId")
    @JvmField
    var modId: Int = 0

    @SerializedName("title")
    @JvmField
    var title: String? = null

    @SerializedName("description")
    @JvmField
    var description: String? = null

    @SerializedName("thumbnailUrl")
    @JvmField
    var thumbnailUrl: String? = null

    @SerializedName("url")
    @JvmField
    var url: String? = null
}
