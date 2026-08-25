package org.levimc.launcher.ui.dialogs.gameversionselect

import org.levimc.launcher.R
import org.levimc.launcher.core.versions.GameVersion
import java.util.ArrayList
import java.util.LinkedHashMap
import kotlin.math.max

object VersionUtil {

    class GroupByResult {
        @JvmField val validGroups: LinkedHashMap<String, VersionGroup> = LinkedHashMap()
        @JvmField val errorGroups: LinkedHashMap<String, VersionGroup> = LinkedHashMap()
    }

    @JvmStatic
    fun buildBigGroups(installed: List<GameVersion>, custom: List<GameVersion>): List<BigGroup> {
        val bigGroups = ArrayList<BigGroup>()

        if (installed.isNotEmpty()) {
            val groupResult = groupByVersion(installed)
            if (groupResult.validGroups.isNotEmpty()) {
                val bg = BigGroup(R.string.installed_packages)
                bg.versionGroups.addAll(groupResult.validGroups.values)
                bigGroups.add(bg)
            }
            if (groupResult.errorGroups.isNotEmpty()) {
                val bg = BigGroup(R.string.error_versions)
                bg.versionGroups.addAll(groupResult.errorGroups.values)
                bigGroups.add(bg)
            }
        }
        if (custom.isNotEmpty()) {
            val groupResult = groupByVersion(custom)
            if (groupResult.validGroups.isNotEmpty()) {
                val bg = BigGroup(R.string.local_custom)
                bg.versionGroups.addAll(groupResult.validGroups.values)
                bigGroups.add(bg)
            }
            if (groupResult.errorGroups.isNotEmpty()) {
                val bg = BigGroup(R.string.error_versions)
                bg.versionGroups.addAll(groupResult.errorGroups.values)
                bigGroups.add(bg)
            }
        }
        return bigGroups
    }

    @JvmStatic
    fun groupByVersion(list: List<GameVersion>): GroupByResult {
        val validMap = LinkedHashMap<String, VersionGroup>()
        val errorMap = LinkedHashMap<String, VersionGroup>()

        for (gv in list) {
            val code = gv.versionCode
            val valid = isValidVersion(code)
            val map = if (valid) validMap else errorMap
            var vg = map[code]
            if (vg == null) {
                vg = VersionGroup(code)
                map[code] = vg
            }
            vg.versions.add(gv)
        }

        val validKeys = ArrayList(validMap.keys)
        val errorKeys = ArrayList(errorMap.keys)

        validKeys.sortWith { a, b -> compareVersionCode(b, a) }
        errorKeys.sort()

        val result = GroupByResult()
        for (key in validKeys) {
            validMap[key]?.let { result.validGroups.put(key, it) }
        }
        for (key in errorKeys) {
            errorMap[key]?.let { result.errorGroups.put(key, it) }
        }

        return result
    }

    @JvmStatic
    fun compareVersionCode(v1: String, v2: String): Int {
        val v1Valid = isValidVersion(v1)
        val v2Valid = isValidVersion(v2)

        if (!v1Valid && !v2Valid) {
            return v1.compareTo(v2)
        }
        if (!v1Valid) {
            return 1
        }
        if (!v2Valid) {
            return -1
        }
        val arr1 = v1.split("\\.".toRegex()).toTypedArray()
        val arr2 = v2.split("\\.".toRegex()).toTypedArray()
        val len = max(arr1.size, arr2.size)
        for (i in 0 until len) {
            val n1 = if (i < arr1.size) arr1[i].toIntOrNull() ?: 0 else 0
            val n2 = if (i < arr2.size) arr2[i].toIntOrNull() ?: 0 else 0
            if (n1 != n2) return n1 - n2
        }
        return 0
    }

    @JvmStatic
    fun isValidVersion(v: String?): Boolean {
        if (v == null) return false
        val arr = v.split("\\.".toRegex()).toTypedArray()
        for (s in arr) {
            if (!s.matches("\\d+".toRegex())) {
                return false
            }
        }
        return true
    }
}
