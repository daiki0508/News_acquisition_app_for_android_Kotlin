package com.websarva.wings.android.newsapp_kotlin.model

import androidx.annotation.Keep

@Keep
data class Search(
    val value: List<Value>
)

@Keep
data class Value(
    var title: String,
    var url: String,
    var image: Image
)

@Keep
data class Image(
    var url: String? = ""
)
