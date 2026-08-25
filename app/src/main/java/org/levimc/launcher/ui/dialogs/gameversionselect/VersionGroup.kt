package org.levimc.launcher.ui.dialogs.gameversionselect

import org.levimc.launcher.core.versions.GameVersion
import java.util.ArrayList

class VersionGroup(@JvmField val versionCode: String) {
    @JvmField val versions: MutableList<GameVersion> = ArrayList()
}
