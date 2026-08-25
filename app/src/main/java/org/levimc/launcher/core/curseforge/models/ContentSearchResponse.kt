package org.levimc.launcher.core.curseforge.models

import com.google.gson.annotations.SerializedName

class ContentSearchResponse {
    @SerializedName("data")
    @JvmField
    var data: List<Content>? = null

    @SerializedName("pagination")
    @JvmField
    var pagination: Pagination? = null
}
