package com.websarva.wings.android.newsapp_kotlin.model

import androidx.annotation.Keep

@Keep
data class Weather(
    val description: Description,
    val forecasts: List<Forecasts>,
    val location: Location
)

@Keep
data class Description(
    var text: String
)

@Keep
data class Forecasts(
    var dateLabel: String,
    var telop: String,
    var detail: Detail,
    val temperature: Temperature,
    val image: ImageView
)

@Keep
data class Detail(
    var wind: String? = "不明",
    var wave: String? = "不明"
)

@Keep
data class Temperature(
    var min: MinMax,
    var max: MinMax,
)

@Keep
data class ImageView(
    val url: String
)

@Keep
data class MinMax(
    var celsius: String? = "不明"
)

@Keep
data class Location(
    var prefecture: String
)