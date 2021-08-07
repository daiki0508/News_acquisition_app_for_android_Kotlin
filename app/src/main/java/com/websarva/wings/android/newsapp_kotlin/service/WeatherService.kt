package com.websarva.wings.android.newsapp_kotlin.service

import androidx.annotation.Keep
import com.websarva.wings.android.newsapp_kotlin.model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    fun getRawRequestForWeather(@Query("city")city: String): Call<Weather>
}