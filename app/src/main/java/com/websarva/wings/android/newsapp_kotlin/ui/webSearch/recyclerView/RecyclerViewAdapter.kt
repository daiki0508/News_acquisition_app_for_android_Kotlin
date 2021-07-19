package com.websarva.wings.android.newsapp_kotlin.ui.webSearch.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.newsapp_kotlin.R

class RecyclerViewAdapter(var items: MutableList<MutableMap<String, String?>>): RecyclerView.Adapter<RecyclerViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.getPrefecture.text = items[position]["prefecture"]
        holder.dateLabel.text = items[position]["dateLabel"]
        holder.telop.text = items[position]["telop"]
        holder.max.text = items[position]["max"]
        holder.min.text = items[position]["min"]
        val describe = items[position]["describe"]
        if (!describe.isNullOrBlank()){
            holder.describe.text = describe
        }else{
            holder.describe.layoutParams.height = 300
            holder.describe.text = ""
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}