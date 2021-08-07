package com.websarva.wings.android.newsapp_kotlin.model

import androidx.annotation.Keep

@Keep
data class ShortUrl(
    val data: Data
)

@Keep
data class Data(
    val url: String
)
