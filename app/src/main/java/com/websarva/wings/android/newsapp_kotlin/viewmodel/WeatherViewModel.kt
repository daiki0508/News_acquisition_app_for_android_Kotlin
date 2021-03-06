package com.websarva.wings.android.newsapp_kotlin.viewmodel

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
import com.websarva.wings.android.newsapp_kotlin.repository.TranslateRepository
import com.websarva.wings.android.newsapp_kotlin.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

@Suppress("BlockingMethodInNonBlockingContext")
class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val translateRepository: TranslateRepository
): ViewModel() {
    private val _weatherList = MutableLiveData<MutableList<MutableMap<String, String?>>>().apply {
        MutableLiveData<MutableList<MutableMap<String, Any>>>()
    }
    private val _imageFlag = MutableLiveData<Int>().apply {
        MutableLiveData<Int>()
    }
    private var weatherList: MutableList<MutableMap<String, String?>>

    @UiThread
    fun receiveWeatherDataGet(areaCode: String,outputLang: String, progressBar: ProgressBar){
        val get = weatherRepository.getRawRequestForWeather(areaCode)

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
                if (it.forecasts[2].detail.wind.isNullOrBlank()){
                    it.forecasts[2].detail.wind = "不明"
                }
                if (it.forecasts[0].detail.wave.isNullOrBlank()){
                    it.forecasts[0].detail.wave = "不明"
                    it.forecasts[1].detail.wave = "不明"
                    it.forecasts[2].detail.wave = "不明"
                }else{
                    it.forecasts[2].detail.wave = "不明"
                }
                when {
                    Regex("^晴").containsMatchIn(it.forecasts[0].telop) -> _imageFlag.value = 0
                    Regex("^曇").containsMatchIn(it.forecasts[0].telop) -> _imageFlag.value = 1
                    else -> _imageFlag.value = 2
                }
                progressBar.progress = 20
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

    @WorkerThread
    private suspend fun receiveTranslateData(weather: Weather,outputLang: String, progressBar: ProgressBar){
        val code = CommonClass(outputLang).code
        if (code != "ja"){
            for (i in 0..14){
                val params: MutableMap<String, String?> = hashMapOf()
                when(i){
                    0 -> params += hashMapOf(
                        "text" to weather.forecasts[0].dateLabel
                    )
                    1 -> params += hashMapOf(
                        "text" to weather.forecasts[1].dateLabel
                    )
                    2 -> params += hashMapOf(
                        "text" to weather.forecasts[2].dateLabel
                    )
                    3 -> params += hashMapOf(
                        "text" to weather.forecasts[0].telop
                    )
                    4 -> params += hashMapOf(
                        "text" to weather.forecasts[1].telop
                    )
                    5 -> params += hashMapOf(
                        "text" to weather.forecasts[2].telop
                    )
                    6 -> params += hashMapOf(
                        "text" to weather.forecasts[0].temperature.max.celsius
                    )
                    7 -> params += hashMapOf(
                        "text" to weather.forecasts[0].temperature.min.celsius
                    )
                    8 -> params += hashMapOf(
                        "text" to weather.forecasts[0].detail.wind
                    )
                    9 -> params += hashMapOf(
                        "text" to weather.forecasts[0].detail.wave
                    )
                    10 -> params += hashMapOf(
                        "text" to weather.forecasts[1].detail.wind
                    )
                    11-> params += hashMapOf(
                        "text" to weather.forecasts[1].detail.wave
                    )
                    12 -> params += hashMapOf(
                        "text" to weather.forecasts[2].detail.wind
                    )
                    13 -> params += hashMapOf(
                        "text" to weather.forecasts[2].detail.wave
                    )
                    else -> params += hashMapOf(
                        "text" to weather.description.text
                    )
                }
                params += hashMapOf(
                    "source" to "ja",
                    "target" to code
                )

                progressBar.progress += 2

                val get = translateRepository.getRawRequestForTranslate(params.toMap())
                val responseBody = translateDataBackGroundRunner(get)
                responseBody.body()?.let {
                    //Log.d("test", "$i : ${it.text}")
                    when (i) {
                        0 -> weather.forecasts[0].dateLabel = it.text
                        1 -> weather.forecasts[1].dateLabel = it.text
                        2 -> weather.forecasts[2].dateLabel = it.text
                        3 -> weather.forecasts[0].telop = it.text
                        4 -> weather.forecasts[1].telop = it.text
                        5 -> weather.forecasts[2].telop = it.text
                        6 -> weather.forecasts[0].temperature.max.celsius = it.text
                        7 -> weather.forecasts[0].temperature.min.celsius = it.text
                        8 -> weather.forecasts[0].detail.wind = it.text
                        9 -> weather.forecasts[0].detail.wave = it.text
                        10 -> weather.forecasts[1].detail.wind = it.text
                        11 -> weather.forecasts[1].detail.wave = it.text
                        12 -> weather.forecasts[2].detail.wind = it.text
                        13 -> weather.forecasts[2].detail.wave = it.text
                        else -> weather.description.text = it.text
                    }
                }
                progressBar.progress += 3
            }
        }else{
            progressBar.progress = 90
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

    @WorkerThread
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
            Log.d("test", it.forecasts[i].image.url)
            weather += mutableMapOf(
                "image" to it.forecasts[i].image.url,
                "dateLabel" to "(${it.forecasts[i].dateLabel}):",
                "telop" to it.forecasts[i].telop,
                "max" to "${it.forecasts[i].temperature.max.celsius}℃",
                "min" to "${it.forecasts[i].temperature.min.celsius}℃",
                "wind" to it.forecasts[i].detail.wind,
                "wave" to it.forecasts[i].detail.wave
            )
            weatherList.add(weather)
            progressBar.progress += 1
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