package com.websarva.wings.android.newsapp_kotlin.ui.weather.recyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.newsapp_kotlin.R

class RecyclerViewHolder(var view: View): RecyclerView.ViewHolder(view){
    val ivWeather: ImageView = view.findViewById(R.id.ivWeather)
    val dateLabel: TextView = view.findViewById(R.id.dateLabel)
    val telop : TextView = view.findViewById(R.id.today_telop)
    val max: TextView = view.findViewById(R.id.today_max)
    val min: TextView = view.findViewById(R.id.today_min)
    val wind: TextView = view.findViewById(R.id.wind)
    val wave: TextView = view.findViewById(R.id.wave)
    val describe: TextView = view.findViewById(R.id.weather_describe)
}