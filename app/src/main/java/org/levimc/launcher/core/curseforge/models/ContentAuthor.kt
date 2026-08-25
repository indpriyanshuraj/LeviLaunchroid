package org.levimc.launcher.core.curseforge.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ContentAuthor : Serializable {
    @SerializedName("id")
    @JvmField
    var id: Int = 0

    @SerializedName("name")
    @JvmField
    var name: String? = null

    @SerializedName("url")
    @JvmField
    var url: String? = null
}
