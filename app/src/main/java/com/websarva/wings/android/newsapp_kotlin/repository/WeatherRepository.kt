package com.websarva.wings.android.newsapp_kotlin.repository

import com.websarva.wings.android.newsapp_kotlin.model.Weather
import com.websarva.wings.android.newsapp_kotlin.service.WeatherService
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLPeerUnverifiedException

class WeatherRepository: WeatherService {
    override fun getRawRequestForWeather(city: String): Call<Weather> {
        val certificatePinner = CertificatePinner.Builder().apply {
            add("weather.tsukumijima.net","sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=")
        }.build()
        val okHttpBuilder = OkHttpClient.Builder().apply {
            certificatePinner(certificatePinner)
            hostnameVerifier { s, sslSession ->
                if (!s.equals(sslSession.peerHost)) {
                    throw SSLPeerUnverifiedException("Invalid Hostname")
                }
                return@hostnameVerifier true
            }
        }.build()
        val retrofit = Retrofit.Builder().apply {
            client(okHttpBuilder)
            baseUrl("https://weather.tsukumijima.net/api/")
            addConverterFactory(GsonConverterFactory.create())
        }.build()
        val service = retrofit.create(WeatherService::class.java)

        return service.getRawRequestForWeather(city)
    }
}