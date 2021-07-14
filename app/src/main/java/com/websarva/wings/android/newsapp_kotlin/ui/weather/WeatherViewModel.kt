package com.websarva.wings.android.newsapp_kotlin.ui.weather

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
    private val _weatherList = MutableLiveData<MutableList<MutableMap<String, Any>>>().apply {
        MutableLiveData<MutableList<MutableMap<String, Any>>>()
    }
    private var weatherList: MutableList<MutableMap<String, Any>>

    @UiThread
    fun receiveWeatherDataGet(get: Call<Weather>, outputLang: String){
        viewModelScope.launch {
            val responseBody = weatherDataBackGroundRunner(get)
            responseBody.body()?.let {
                receiveTranslateData(it, outputLang)
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
    private suspend fun receiveTranslateData(weather: Weather, outputLang: String){
        val code = CommonClass(outputLang).code
        if (code != "ja"){
            for (i in 0..4){
                val params: Map<String, String> = when(i){
                    0 -> hashMapOf(
                        "text" to weather.location.prefecture,
                        "source" to "en",
                        "target" to code
                    )
                    1 -> hashMapOf(
                        "text" to weather.forecasts[0].telop,
                        "source" to "en",
                        "target" to code
                    )
                    2 -> hashMapOf(
                        "text" to weather.forecasts[1].telop,
                        "source" to "en",
                        "target" to code
                    )
                    3 -> hashMapOf(
                        "text" to weather.forecasts[2].telop,
                        "source" to "en",
                        "target" to code
                    )
                    else -> hashMapOf(
                        "text" to weather.description.text,
                        "source" to "en",
                        "target" to code
                    )
                }
                val get = WeatherActivity().serviceTranslate.getRawRequestForTranslate(params)
                val responseBody = translateDataBackGroundRunner(get)
                responseBody.body()?.let {
                    when (i) {
                        0 -> weather.location.prefecture = it.text
                        1 -> weather.forecasts[0].telop = it.text
                        2 -> weather.forecasts[1].telop = it.text
                        3 -> weather.forecasts[2].telop = it.text
                        else -> weather.description.text = it.text
                    }
                }
            }
        }
        setWeatherData(weather)
    }

    @WorkerThread
    private suspend fun translateDataBackGroundRunner(get: Call<Translate>): Response<Translate>{
        return withContext(Dispatchers.IO){
            val responseBody = get.execute()

            responseBody
        }
    }

    @UiThread
    private fun setWeatherData(it: Weather){
        for (i in 0..2){
            when(i){
                0 -> {
                    val weather = mutableMapOf(
                        "prefecture" to it.location.prefecture,
                        "dateLabel" to "(${it.forecasts[0].dateLabel}):",
                        "telop" to it.forecasts[0].telop,
                        "max" to it.forecasts[0].temperature.max,
                        "min" to it.forecasts[0].temperature.min,
                        "describe" to it.description
                        )
                    weatherList.add(weather)
                }
                1 -> {
                    val weather = mutableMapOf(
                        "prefecture" to it.location.prefecture,
                        "dateLabel" to "(${it.forecasts[1].dateLabel}):",
                        "telop" to it.forecasts[1].telop,
                        "max" to it.forecasts[1].temperature.max,
                        "min" to it.forecasts[1].temperature.min,
                        "describe" to "",
                    )
                    weatherList.add(weather)
                }
                else -> {
                    val weather = mutableMapOf(
                        "prefecture" to it.location.prefecture,
                        "dateLabel" to it.forecasts[2].dateLabel,
                        "telop" to it.forecasts[2].telop,
                        "max" to it.forecasts[2].temperature.max,
                        "min" to it.forecasts[2].temperature.min,
                        "describe" to ""
                    )
                    weatherList.add(weather)
                    _weatherList.postValue(weatherList)
                }
            }
        }
    }

    fun weatherList(): MutableLiveData<MutableList<MutableMap<String, Any>>>{
        return _weatherList
    }

    init {
        _weatherList.value = mutableListOf()
        weatherList = mutableListOf()
    }
}