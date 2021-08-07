package com.websarva.wings.android.newsapp_kotlin

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.websarva.wings.android.newsapp_kotlin.service.TranslateService
import com.websarva.wings.android.newsapp_kotlin.ui.license.LicenseActivity
import com.websarva.wings.android.newsapp_kotlin.ui.settings.SettingsActivity
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLPeerUnverifiedException

class CommonClass(outputLang: String?){
    val code: String = when (outputLang) {
        "ニュースアプリ" -> "ja"
        "新闻应用" -> "zh"
        "뉴스 애플 리케이션" -> "ko"
        "Приложение новостей" -> "ru"
        else -> "en"
    }

    /*private val certificatePinner = CertificatePinner.Builder().apply {
        add("script.google.com", "sha256/glbBfseqsU1YNZ88LPRcV1X3wkwrtKiFvCONZiMdxbc=")
        add("script.googleusercontent.com", "sha256/wHidWBJ3G2vSPVXbm/csvf1rOza5bctUvEOrsf1+1Qw=")
    }.build()*/
    /*private val okHttpClient = OkHttpClient.Builder().apply {
        //certificatePinner(certificatePinner)
        hostnameVerifier { s, sslSession ->
            if (!s.equals(sslSession.peerHost)){
                throw SSLPeerUnverifiedException("Invalid Hostname")
            }
            return@hostnameVerifier true
        }
    }.build()
    private val retrofitTranslate: Retrofit = Retrofit.Builder().apply {
        client(okHttpClient)
        baseUrl("https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/")
        addConverterFactory(GsonConverterFactory.create())
    }.build()
    val serviceTranslate: TranslateService = retrofitTranslate.create(TranslateService::class.java)*/

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