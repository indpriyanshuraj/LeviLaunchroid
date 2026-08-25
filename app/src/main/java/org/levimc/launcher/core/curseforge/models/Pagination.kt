package org.levimc.launcher.core.curseforge.models

import com.google.gson.annotations.SerializedName

class Pagination {
    @SerializedName("index")
    @JvmField
    var index: Int = 0

    @SerializedName("totalCount")
    @JvmField
    var totalCount: Long = 0
}
