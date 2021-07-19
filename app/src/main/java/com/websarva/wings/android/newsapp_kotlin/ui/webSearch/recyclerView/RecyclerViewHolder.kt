package com.websarva.wings.android.newsapp_kotlin.ui.webSearch.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.newsapp_kotlin.R

class RecyclerViewHolder(var view: View): RecyclerView.ViewHolder(view){
    val getPrefecture: TextView = view.findViewById(R.id.get_prefecture)
    val dateLabel: TextView = view.findViewById(R.id.dateLabel)
    val telop : TextView = view.findViewById(R.id.today_telop)
    val max: TextView = view.findViewById(R.id.today_max)
    val min: TextView = view.findViewById(R.id.today_min)
    val describe: TextView = view.findViewById(R.id.weather_describe)
}