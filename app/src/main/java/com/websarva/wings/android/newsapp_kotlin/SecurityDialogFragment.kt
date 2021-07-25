package com.websarva.wings.android.newsapp_kotlin

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class SecurityDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.root_title)
                .setMessage(R.string.root_message)
                .setPositiveButton("OK"){_, _ ->
                    it.finish()
                }
                .create()
        }
        this.isCancelable = false

        return dialog?: throw IllegalStateException("activityがnullです。")
    }
}