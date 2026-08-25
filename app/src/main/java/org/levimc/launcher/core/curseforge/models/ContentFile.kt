package org.levimc.launcher.core.curseforge.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ContentFile : Serializable {
    @SerializedName("id")
    @JvmField
    var id: Int = 0

    @SerializedName("modId")
    @JvmField
    var modId: Int = 0

    @SerializedName("displayName")
    @JvmField
    var displayName: String? = null

    @SerializedName("fileName")
    @JvmField
    var fileName: String? = null

    @SerializedName("hashes")
    @JvmField
    var hashes: List<FileHash>? = null

    @SerializedName("fileDate")
    @JvmField
    var fileDate: String? = null

    @SerializedName("downloadUrl")
    @JvmField
    var downloadUrl: String? = null

    @SerializedName("gameVersions")
    @JvmField
    var gameVersions: List<String>? = null

    @SerializedName("dependencies")
    @JvmField
    var dependencies: List<FileDependency>? = null

    @SerializedName("modules")
    @JvmField
    var modules: List<FileModule>? = null

    class FileHash : Serializable {
        @SerializedName("value")
        @JvmField
        var value: String? = null

        @SerializedName("algo")
        @JvmField
        var algo: Int = 0
    }

    class FileDependency : Serializable {
        @SerializedName("modId")
        @JvmField
        var modId: Int = 0
    }

    class FileModule : Serializable {
        @SerializedName("name")
        @JvmField
        var name: String? = null

        @SerializedName("fingerprint")
        @JvmField
        var fingerprint: Long = 0
    }
}
