package org.levimc.launcher.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.levimc.launcher.R
import org.levimc.launcher.core.versions.GameVersion
import org.levimc.launcher.core.versions.VersionManager
import org.levimc.launcher.ui.animation.DynamicAnim
import org.levimc.launcher.ui.dialogs.gameversionselect.BigGroup
import org.levimc.launcher.ui.dialogs.gameversionselect.UltimateVersionAdapter

class GameVersionSelectDialog(ctx: Context, private val bigGroups: List<BigGroup>) : Dialog(ctx) {

    interface OnVersionSelectListener {
        fun onVersionSelected(version: GameVersion)
    }

    private var listener: OnVersionSelectListener? = null

    fun setOnVersionSelectListener(l: OnVersionSelectListener?) {
        this.listener = l
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_game_version_select)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_versions)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = UltimateVersionAdapter(context, bigGroups)
        adapter.setOnVersionSelectListener(object : UltimateVersionAdapter.OnVersionSelectListener {
            override fun onVersionSelected(version: GameVersion) {
                listener?.onVersionSelected(version)
                dismiss()
            }
        })

        adapter.setOnVersionLongClickListener(object : UltimateVersionAdapter.OnVersionLongClickListener {
            override fun onVersionLongClicked(version: GameVersion) {
                showRenameDialog(version)
            }
        })

        val window = window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            val params = window.attributes
            params.dimAmount = 0.6f

            val density = context.resources.displayMetrics.density
            val screenWidth = context.resources.displayMetrics.widthPixels
            val maxWidth = (420 * density).toInt()
            params.width = (screenWidth * 0.95).toInt().coerceAtMost(maxWidth)
            window.attributes = params
        }
        recyclerView.adapter = adapter

        // Intro spring animation
        val root = findViewById<View>(android.R.id.content)
        if (root != null) {
            val dy = context.resources.displayMetrics.density * 12f
            root.alpha = 0f
            root.translationY = dy
            DynamicAnim.springAlphaTo(root, 1f).start()
            DynamicAnim.springTranslationYTo(root, 0f).start()
        }
        recyclerView.post { DynamicAnim.staggerRecyclerChildren(recyclerView) }
    }

    private fun showRenameDialog(version: GameVersion) {
        val customView = layoutInflater.inflate(R.layout.dialog_rename_entry, null)
        val editName = customView.findViewById<EditText>(R.id.edit_version_name)
        val errorText = customView.findViewById<View>(R.id.text_version_error)

        val currentName = extractDisplayName(version)
        editName.setText(currentName)
        if (currentName != null) {
            editName.setSelection(currentName.length)
        }

        val renameDialog = CustomAlertDialog(context)
            .setTitleText(context.getString(R.string.rename_version_title))
            .setCustomView(customView)
            .setUseBorderedBackground(true)
            .setBlurBackground(true)
            .setPositiveButton(context.getString(R.string.rename)) { _ ->
                val newName = editName.text.toString().trim()
                if (isValidName(newName)) {
                    performRename(version, newName)
                }
            }
            .setNegativeButton(context.getString(R.string.cancel), null)

        renameDialog.show()

        editName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val valid = isValidName(s.toString().trim())
                errorText.visibility = if (valid) View.GONE else View.VISIBLE
                renameDialog.positiveButton.isEnabled = valid
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun extractDisplayName(version: GameVersion?): String? {
        if (version == null) return ""
        val displayName = version.displayName
        if (displayName == null) return version.directoryName
        val lastParenIndex = displayName.lastIndexOf(" (")
        if (lastParenIndex > 0) return displayName.substring(0, lastParenIndex)
        return version.directoryName
    }

    private fun isValidName(name: String?): Boolean {
        if (name.isNullOrEmpty() || name.length > 40) return false
        return Regex("^[a-zA-Z0-9._-]+$").matches(name)
    }

    private fun performRename(version: GameVersion, newName: String) {
        VersionManager.get(context).renameVersion(version, newName, object : VersionManager.OnRenameVersionCallback {
            override fun onRenameCompleted(success: Boolean) {
                Handler(Looper.getMainLooper()).post {
                    if (success) {
                        Toast.makeText(context, context.getString(R.string.rename_success), Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }
            }

            override fun onRenameFailed(e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, context.getString(R.string.rename_failed, e.message), Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
