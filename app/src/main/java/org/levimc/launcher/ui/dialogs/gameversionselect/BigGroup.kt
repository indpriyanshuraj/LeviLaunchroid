package org.levimc.launcher.ui.dialogs.gameversionselect

import androidx.annotation.StringRes
import java.util.ArrayList

class BigGroup(@param:StringRes @JvmField val groupTitleResId: Int) {
    @JvmField val versionGroups: MutableList<VersionGroup> = ArrayList()
}
