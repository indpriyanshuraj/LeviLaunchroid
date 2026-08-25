package org.levimc.launcher.core.curseforge.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Content : Serializable {
    @SerializedName("id")
    @JvmField
    var id: Int = 0

    @SerializedName("name")
    @JvmField
    var name: String? = null

    @SerializedName("links")
    @JvmField
    var links: ContentLinks? = null

    @SerializedName("summary")
    @JvmField
    var summary: String? = null

    @SerializedName("status")
    @JvmField
    var status: Int = 0

    @SerializedName("downloadCount")
    @JvmField
    var downloadCount: Long = 0

    @SerializedName("categories")
    @JvmField
    var categories: List<ContentCategory>? = null

    @SerializedName("authors")
    @JvmField
    var authors: List<ContentAuthor>? = null

    @SerializedName("logo")
    @JvmField
    var logo: ContentAsset? = null

    @SerializedName("latestFiles")
    @JvmField
    var latestFiles: List<ContentFile>? = null

    @SerializedName("dateModified")
    @JvmField
    var dateModified: String? = null
}
