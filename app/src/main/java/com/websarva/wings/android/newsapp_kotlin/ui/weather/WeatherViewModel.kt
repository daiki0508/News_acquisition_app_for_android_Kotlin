package com.websarva.wings.android.newsapp_kotlin.ui.weather

import android.util.Log
import android.widget.ProgressBar
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.android.newsapp_kotlin.CommonClass
import com.websarva.wings.android.newsapp_kotlin.model.Translate
import com.websarva.wings.android.newsapp_kotlin.model.Weather
import com.websarva.wings.android.newsapp_kotlin.service.TranslateService
import com.websarva.wings.android.newsapp_kotlin.ui.webSearch.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("BlockingMethodInNonBlockingContext")
class WeatherViewModel: ViewModel() {
    private val _weatherList = MutableLiveData<MutableList<MutableMap<String, String?>>>().apply {
        MutableLiveData<MutableList<MutableMap<String, Any>>>()
    }
    private val _imageFlag = MutableLiveData<Int>().apply {
        MutableLiveData<Int>()
    }
    private var weatherList: MutableList<MutableMap<String, String?>>

    @UiThread
    fun receiveWeatherDataGet(get: Call<Weather>, outputLang: String, progressBar: ProgressBar){
        viewModelScope.launch {
            weatherList = mutableListOf()
            progressBar.progress = 5

            val responseBody = weatherDataBackGroundRunner(get)
            responseBody.body()?.let {
                progressBar.progress = 20
                if (it.forecasts[0].temperature.min.celsius.isNullOrBlank()){
                    it.forecasts[0].temperature.min.celsius = "不明"
                }
                if (it.forecasts[0].temperature.max.celsius.isNullOrBlank()){
                    it.forecasts[0].temperature.max.celsius = "不明"
                }
                when {
                    Regex("^晴").containsMatchIn(it.forecasts[0].telop) -> _imageFlag.value = 0
                    Regex("^曇").containsMatchIn(it.forecasts[0].telop) -> _imageFlag.value = 1
                    else -> _imageFlag.value = 2
                }
                progressBar.progress = 30
                receiveTranslateData(it, outputLang, progressBar)
            }
        }
    }

    @WorkerThread
    private suspend fun weatherDataBackGroundRunner(get: Call<Weather>): Response<Weather>{
        return withContext(Dispatchers.IO){
            val responseBody = get.execute()

            responseBody
        }
    }

    @UiThread
    private suspend fun receiveTranslateData(weather: Weather,outputLang: String, progressBar: ProgressBar){
        val code = CommonClass(outputLang).code
        if (code != "ja"){
            for (i in 0..6){
                val params: MutableMap<String, String?> = hashMapOf()
                when(i){
                    0 -> params += hashMapOf(
                        "text" to weather.location.prefecture
                    )
                    1 -> params += hashMapOf(
                        "text" to weather.forecasts[0].telop
                    )
                    2 -> params += hashMapOf(
                        "text" to weather.forecasts[1].telop
                    )
                    3 -> params += hashMapOf(
                        "text" to weather.forecasts[2].telop
                    )
                    4 -> params += hashMapOf(
                        "text" to weather.forecasts[0].temperature.max.celsius
                    )
                    5 -> params += hashMapOf(
                        "text" to weather.forecasts[0].temperature.min.celsius
                    )
                    else -> params += hashMapOf(
                        "text" to weather.description.text
                    )
                }
                params += hashMapOf(
                    "source" to "ja",
                    "target" to code
                )

                progressBar.progress += 4

                val get = CommonClass(null).serviceTranslate.getRawRequestForTranslate(params.toMap())
                val responseBody = translateDataBackGroundRunner(get)
                responseBody.body()?.let {
                    when (i) {
                        0 -> weather.location.prefecture = it.text
                        1 -> weather.forecasts[0].telop = it.text
                        2 -> weather.forecasts[1].telop = it.text
                        3 -> weather.forecasts[2].telop = it.text
                        4 -> weather.forecasts[0].temperature.max.celsius = it.text
                        5 -> weather.forecasts[0].temperature.min.celsius = it.text
                        else -> weather.description.text = it.text
                    }
                }
                progressBar.progress += 5
            }
        }else{
            progressBar.progress = 84
        }
        setWeatherData(weather, progressBar)
    }

    @WorkerThread
    private suspend fun translateDataBackGroundRunner(get: Call<Translate>): Response<Translate>{
        return withContext(Dispatchers.IO){
            val responseBody = get.execute()

            responseBody
        }
    }

    @UiThread
    private fun setWeatherData(it: Weather, progressBar: ProgressBar){
        for (i in 0..2){
            val weather = mutableMapOf<String, String?>()
            when(i){
                0 -> {
                    weather += mutableMapOf(
                        "describe" to it.description.text
                    )
                }
                1 -> {
                    weather += mutableMapOf(
                        "describe" to ""
                    )
                }
                else -> {
                    weather += mutableMapOf(
                        "describe" to ""
                    )
                }
            }
            weather += mutableMapOf(
                "prefecture" to it.location.prefecture,
                "dateLabel" to "(${it.forecasts[i].dateLabel}):",
                "telop" to it.forecasts[i].telop,
                "max" to "${it.forecasts[i].temperature.max.celsius}℃",
                "min" to "${it.forecasts[i].temperature.min.celsius}℃",
            )
            weatherList.add(weather)
            progressBar.progress += 2
        }
        _weatherList.postValue(weatherList)
    }

    fun weatherList(): MutableLiveData<MutableList<MutableMap<String, String?>>>{
        return _weatherList
    }
    fun imageFlag(): MutableLiveData<Int>{
        return _imageFlag
    }

    init {
        _weatherList.value = mutableListOf()
        weatherList = mutableListOf()
        _imageFlag.value = 0
    }
}