package com.websarva.wings.android.newsapp_kotlin.ui.webSearch

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.websarva.wings.android.newsapp_kotlin.DialogLister
import com.websarva.wings.android.newsapp_kotlin.R
import com.websarva.wings.android.newsapp_kotlin.ui.tweet.TweetActivity
import java.lang.IllegalStateException

class SelectDialog: DialogFragment() {
    private var listener: DialogLister? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.dialog_title)
                .setItems(R.array.select_dialog) { _, which ->
                    if (which == 0){
                        listener?.onDialogFlagReceive(this, flag = true)
                    }else{
                        listener?.onDialogFlagReceive(this, flag = false)
                    }
                }
                .create()
        }
        return dialog ?: throw IllegalStateException("activityがnullです。")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as DialogLister
        }catch (e: Exception){
            Log.e("ERROR", "CANNOT FIND LISTENER")
        }
    }

    override fun onDetach() {
        listener = null

        super.onDetach()
    }
}