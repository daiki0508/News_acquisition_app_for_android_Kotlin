package com.websarva.wings.android.newsapp_kotlin.ui.weather.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.loadAny
import coil.request.ImageRequest
import com.websarva.wings.android.newsapp_kotlin.R

class RecyclerViewAdapter(private var items: MutableList<MutableMap<String, String?>>, context: Context): RecyclerView.Adapter<RecyclerViewHolder>(){
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_weather, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val imageLoader = ImageLoader.Builder(this.context!!)
            .componentRegistry { add(SvgDecoder(context!!)) }
            .build()

        holder.ivWeather.load(items[position]["image"], imageLoader){
            error(R.drawable.no_image)
        }
        holder.dateLabel.text = items[position]["dateLabel"]
        holder.telop.text = items[position]["telop"]
        holder.max.text = items[position]["max"]
        holder.min.text = items[position]["min"]
        holder.wind.text = items[position]["wind"]
        holder.wave.text = items[position]["wave"]
        val describe = items[position]["describe"]
        if (!describe.isNullOrBlank()){
            holder.describe.text = describe
        }else{
            holder.describe.layoutParams.height = 50
            holder.describe.text = ""
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    init {
        this.context = context
    }
}