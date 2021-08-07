package com.websarva.wings.android.newsapp_kotlin.repository

import com.websarva.wings.android.newsapp_kotlin.model.Translate
import com.websarva.wings.android.newsapp_kotlin.service.TranslateService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLPeerUnverifiedException

class TranslateRepository: TranslateService {
    override fun getRawRequestForTranslate(params: Map<String, String?>): Call<Translate> {
        val okHttpClient = OkHttpClient.Builder().apply {
            //certificatePinner(certificatePinner)
            hostnameVerifier { s, sslSession ->
                if (!s.equals(sslSession.peerHost)){
                    throw SSLPeerUnverifiedException("Invalid Hostname")
                }
                return@hostnameVerifier true
            }
        }.build()
        val retrofitTranslate: Retrofit = Retrofit.Builder().apply {
            client(okHttpClient)
            baseUrl("https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/")
            addConverterFactory(GsonConverterFactory.create())
        }.build()
        val serviceTranslate: TranslateService = retrofitTranslate.create(TranslateService::class.java)

        return serviceTranslate.getRawRequestForTranslate(params)
    }
}