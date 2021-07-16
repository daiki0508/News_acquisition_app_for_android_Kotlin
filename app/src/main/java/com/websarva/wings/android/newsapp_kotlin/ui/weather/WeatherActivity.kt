package com.websarva.wings.android.newsapp_kotlin.ui.weather

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.newsapp_kotlin.R
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivityWeatherBinding
import com.websarva.wings.android.newsapp_kotlin.service.TranslateService
import com.websarva.wings.android.newsapp_kotlin.service.WeatherService
import com.websarva.wings.android.newsapp_kotlin.ui.webSearch.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private val viewModel:  WeatherViewModel by viewModel()

    private val retrofit = Retrofit.Builder().apply {
        baseUrl("https://weather.tsukumijima.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
    }.build()
    private val service = retrofit.create(WeatherService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }
        val layoutManager = LinearLayoutManager(this)
        binding.weatherList.layoutManager = layoutManager

        val progressBar = binding.progressbar
        progressBar.visibility = View.GONE

        /*val from = arrayOf("prefecture", "dateLabel", "telop", "max", "min", "describe")
        val to = intArrayOf(R.id.get_prefecture, R.id.dateLabel, R.id.today_telop, R.id.today_max, R.id.today_min, R.id.weather_describe)*/
        var bitMap: Bitmap
        viewModel.weatherList().observe(this, {
            if (!viewModel.weatherList().value.isNullOrEmpty()){
                bitMap = when(viewModel.imageFlag().value){
                    0 -> BitmapFactory.decodeResource(resources, R.drawable.sunny)
                    1 -> BitmapFactory.decodeResource(resources, R.drawable.cloudy)
                    else -> BitmapFactory.decodeResource(resources, R.drawable.rainny)
                }
                binding.ivWeather.setImageBitmap(bitMap)

                val adapter = RecyclerViewAdapter(viewModel.weatherList().value!!)
                binding.weatherList.adapter = adapter
                binding.weatherList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                adapter.notifyDataSetChanged()
                Log.d("test", "Called!!")

                progressBar.progress = 100
                progressBar.visibility = View.GONE
                binding.weatherExecute.isEnabled = true
            }
        })

        binding.weatherExecute.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            binding.weatherExecute.isEnabled = false

            val areaCode = cityCode(binding.searchConditions1List.selectedItemId)
            Log.d("test", areaCode)

            val get = service.getRawRequestForWeather(areaCode)
            viewModel.receiveWeatherDataGet(get, outputLang = getString(R.string.app_name), progressBar)
        }
    }

    private fun cityCode(selected_id: Long): String {
        return when (selected_id.toInt()) {
            in 1..9 -> {
                "0${selected_id + 1}0010"
            }
            in 10..19 -> {
                "${selected_id + 1}0010"
            }
            in 20..29 -> {
                "${selected_id + 1}0010"
            }
            in 30..39 -> {
                "${selected_id + 1}0010"
            }
            in 40..45 -> {
                "${selected_id + 1}0010"
            }
            46 -> {
                "471010"
            }
            else -> {
                "016010"
            }
        }
    }
}

private class RecyclerViewHolder(var view: View): RecyclerView.ViewHolder(view){
    val getPrefecture: TextView = view.findViewById(R.id.get_prefecture)
    //val dateLabel: TextView = view.findViewById(R.id.dateLabel)
    //val telop : TextView = view.findViewById(R.id.today_telop)
    val describe: TextView = view.findViewById(R.id.weather_describe)
}

private class RecyclerViewAdapter(var items: MutableList<MutableMap<String, String?>>): RecyclerView.Adapter<RecyclerViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.row, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.getPrefecture.text = items[position]["prefecture"]
        val describe = items[position]["describe"]
        if (!describe.isNullOrBlank()){
            holder.describe.text = describe
        }else{
            holder.describe.layoutParams.height = 300
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}