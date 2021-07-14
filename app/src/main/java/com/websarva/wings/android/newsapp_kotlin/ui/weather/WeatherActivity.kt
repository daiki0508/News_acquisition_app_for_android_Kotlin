package com.websarva.wings.android.newsapp_kotlin.ui.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SimpleAdapter
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

    private val retrofitTranslate: Retrofit = Retrofit.Builder().apply {
        baseUrl("https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/")
            .addConverterFactory(GsonConverterFactory.create())
    }.build()
    val serviceTranslate: TranslateService = retrofitTranslate.create(TranslateService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val progressBar = binding.progressbar
        progressBar.visibility = View.GONE

        val from = arrayOf("prefecture", "dateLabel", "telop", "max", "min", "describe")
        val to = intArrayOf(R.id.get_prefecture, R.id.dateLabel, R.id.today_telop, R.id.today_max, R.id.today_min, R.id.weather_describe)
        viewModel.weatherList().observe(this, {
            val adapter = SimpleAdapter(this, viewModel.weatherList().value, R.layout.row, from, to)
            binding.weatherList.adapter = adapter
            adapter.notifyDataSetChanged()
        })

        binding.weatherExecute.setOnClickListener {
            val areaCode = cityCode(binding.searchConditions1List.selectedItemId)
            Log.d("test", areaCode)

            val get = service.getRawRequestForWeather(areaCode)
            viewModel.receiveWeatherDataGet(get, outputLang = getString(R.string.app_name))
        }

        binding.tvNews.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    private fun cityCode(selected_id: Long): String{
        var retId = ""

        retId = when (selected_id.toInt()) {
            in 1..9 -> {
                "0${selected_id+1}0010"
            }
            in 10..19 -> {
                "1${selected_id+1}0010"
            }
            in 20..29 -> {
                "2${selected_id+1}0010"
            }
            in 30..39 -> {
                "3${selected_id+1}0010"
            }
            in 40..45 -> {
                "4${selected_id+1}0010"
            }
            46 -> {
                "471010"
            }
            else -> {
                "016010"
            }
        }
        return retId
    }
}