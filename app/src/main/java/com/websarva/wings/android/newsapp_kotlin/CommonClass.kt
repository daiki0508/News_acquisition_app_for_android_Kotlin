package com.websarva.wings.android.newsapp_kotlin

import com.websarva.wings.android.newsapp_kotlin.service.TranslateService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommonClass(outputLang: String?){
    val code: String = when (outputLang) {
        "ニュースアプリ_Kotlin" -> "ja"
        "新闻应用_Kotlin" -> "zh"
        "뉴스 애플 리케이션_Kotlin" -> "ko"
        "Приложение новостей" -> "ru"
        else -> "en"
    }

    private val retrofitTranslate: Retrofit = Retrofit.Builder().apply {
        baseUrl("https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/")
            .addConverterFactory(GsonConverterFactory.create())
    }.build()
    val serviceTranslate: TranslateService = retrofitTranslate.create(TranslateService::class.java)
}