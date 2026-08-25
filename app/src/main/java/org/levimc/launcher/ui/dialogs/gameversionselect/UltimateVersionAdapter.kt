package org.levimc.launcher.ui.dialogs.gameversionselect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.levimc.launcher.R
import org.levimc.launcher.core.versions.GameVersion
import org.levimc.launcher.ui.animation.DynamicAnim
import java.util.ArrayList

class UltimateVersionAdapter(private val context: Context, bigGroups: List<BigGroup>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val flatItems = ArrayList<Any>()
    private var listener: OnVersionSelectListener? = null
    private var longClickListener: OnVersionLongClickListener? = null

    interface OnVersionSelectListener {
        fun onVersionSelected(version: GameVersion)
    }

    interface OnVersionLongClickListener {
        fun onVersionLongClicked(version: GameVersion)
    }

    init {
        for (group in bigGroups) {
            flatItems.add(group.groupTitleResId)
            for (vg in group.versionGroups) {
                flatItems.add(vg.versionCode)
                flatItems.addAll(vg.versions)
            }
        }
    }

    fun setOnVersionSelectListener(l: OnVersionSelectListener?) {
        this.listener = l
    }

    fun setOnVersionLongClickListener(l: OnVersionLongClickListener?) {
        this.longClickListener = l
    }

    override fun getItemViewType(position: Int): Int {
        val item = flatItems[position]
        if (item is Int) return TYPE_BIG_GROUP
        return if (item is String) TYPE_VER_GROUP else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BIG_GROUP -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_version_big_group, parent, false)
                BigGroupVH(v)
            }
            TYPE_VER_GROUP -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_version_group_title, parent, false)
                VerGroupVH(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_version, parent, false)
                ItemVH(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = flatItems[position]
        val viewType = getItemViewType(position)

        when (viewType) {
            TYPE_BIG_GROUP -> {
                val resId = item as Int
                (holder as BigGroupVH).title.text = context.getString(resId)
            }
            TYPE_VER_GROUP -> {
                (holder as VerGroupVH).title.text = item as String
            }
            else -> {
                val gv = item as GameVersion
                (holder as ItemVH).bind(gv, listener, longClickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return flatItems.size
    }

    internal class BigGroupVH(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.tv_big_group_title)
    }

    internal class VerGroupVH(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.tv_version_code_group)
    }

    internal class ItemVH(v: View) : RecyclerView.ViewHolder(v) {
        val tv: TextView = v.findViewById(R.id.tv_version_name_item)
        val parentLayout: LinearLayout = v.findViewById(R.id.linear_parent)
        val btnRename: View? = v.findViewById(R.id.btn_rename)

        fun bind(v: GameVersion, listener: OnVersionSelectListener?, longClickListener: OnVersionLongClickListener?) {
            val sb = java.lang.StringBuilder()
            sb.append(v.displayName)
            if (v.isInstalled && v.packageName != null) {
                sb.append(" ").append(v.packageName)
            }
            tv.text = sb.toString()
            parentLayout.setOnClickListener { _ ->
                listener?.onVersionSelected(v)
            }

            if (!v.isInstalled) {
                if (btnRename != null) {
                    btnRename.visibility = View.VISIBLE
                    btnRename.setOnClickListener { _ ->
                        longClickListener?.onVersionLongClicked(v)
                    }
                    DynamicAnim.applyPressScale(btnRename)
                }
                parentLayout.setOnLongClickListener { _ ->
                    longClickListener?.onVersionLongClicked(v)
                    true
                }
            } else {
                if (btnRename != null) btnRename.visibility = View.GONE
                parentLayout.setOnLongClickListener(null)
            }
        }
    }

    companion object {
        private const val TYPE_BIG_GROUP = 0
        private const val TYPE_VER_GROUP = 1
        private const val TYPE_ITEM = 2
    }
}
