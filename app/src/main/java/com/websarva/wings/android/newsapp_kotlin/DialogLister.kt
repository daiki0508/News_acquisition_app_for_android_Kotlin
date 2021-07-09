package com.websarva.wings.android.newsapp_kotlin

import androidx.fragment.app.DialogFragment

interface DialogLister {
    fun onDialogFlagReceive(dialog: DialogFragment, flag: Boolean)
}