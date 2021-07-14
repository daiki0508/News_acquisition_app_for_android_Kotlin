package com.websarva.wings.android.newsapp_kotlin

class CommonClass(outputLang: String){
    val code: String = when (outputLang) {
        "ニュースアプリ_Kotlin" -> "ja"
        "新闻应用_Kotlin" -> "zh"
        "뉴스 애플 리케이션_Kotlin" -> "ko"
        "Приложение новостей" -> "ru"
        else -> "en"
    }
}