package com.websarva.wings.android.newsapp_kotlin.ui.webSearch.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.newsapp_kotlin.R

class RecyclerViewHolder(var view: View): RecyclerView.ViewHolder(view){
    val title: TextView = view.findViewById(R.id.rvtitle)
    val url: TextView = view.findViewById(R.id.rvurl)
}