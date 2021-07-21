package com.websarva.wings.android.newsapp_kotlin

import android.app.Activity
import android.content.Intent
import com.websarva.wings.android.newsapp_kotlin.service.TranslateService
import com.websarva.wings.android.newsapp_kotlin.ui.license.LicenseActivity
import com.websarva.wings.android.newsapp_kotlin.ui.settings.SettingsActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommonClass(outputLang: String?){
    val code: String = when (outputLang) {
        "ニュースアプリ" -> "ja"
        "新闻应用" -> "zh"
        "뉴스 애플 리케이션" -> "ko"
        "Приложение новостей" -> "ru"
        else -> "en"
    }

    private val retrofitTranslate: Retrofit = Retrofit.Builder().apply {
        baseUrl("https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/")
            .addConverterFactory(GsonConverterFactory.create())
    }.build()
    val serviceTranslate: TranslateService = retrofitTranslate.create(TranslateService::class.java)

    fun settingsIntent(activity: Activity){
        activity.let {
            it.startActivity(Intent(it, SettingsActivity::class.java))
            it.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    fun licenseIntent(activity: Activity){
        activity.let {
            it.startActivity(Intent(it, LicenseActivity::class.java))
            it.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}