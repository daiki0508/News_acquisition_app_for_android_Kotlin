package com.websarva.wings.android.newsapp_kotlin.ui.webSearch.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.websarva.wings.android.newsapp_kotlin.OnItemClickListener
import com.websarva.wings.android.newsapp_kotlin.R

class RecyclerViewAdapter(private var items: MutableList<MutableMap<String, String>>): RecyclerView.Adapter<RecyclerViewHolder>(){
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row_websearch, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.title.text = items[position]["title"]
        holder.url.text = items[position]["url"]
        holder.img.load(items[position]["img"]){
            error(R.drawable.no_image)
        }

        holder.view.setOnClickListener {
            listener.onItemClickListener(it, position, items[position]["url"]!!)
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return items.size
    }
}