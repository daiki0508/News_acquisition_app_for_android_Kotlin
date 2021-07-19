package com.websarva.wings.android.newsapp_kotlin

import android.view.View

interface OnItemClickListener {
    fun onItemClickListener(view: View, position: Int, clickedText: String)
}