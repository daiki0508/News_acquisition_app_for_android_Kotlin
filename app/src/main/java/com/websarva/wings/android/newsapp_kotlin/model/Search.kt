package com.websarva.wings.android.newsapp_kotlin.model

data class Search(
    val value: List<Value>
)

data class Value(
    val title: String,
    val url: String
)
