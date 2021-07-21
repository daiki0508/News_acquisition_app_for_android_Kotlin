package com.websarva.wings.android.newsapp_kotlin.model

data class Weather(
    val description: Description,
    val forecasts: List<Forecasts>,
    val location: Location
)

data class Description(
    var text: String
)

data class Forecasts(
    val dateLabel: String,
    var telop: String,
    var detail: Detail,
    val temperature: Temperature,
)

data class Detail(
    var wind: String? = "不明",
    var wave: String? = "不明"
)

data class Temperature(
    var min: MinMax,
    var max: MinMax,
)

data class MinMax(
    var celsius: String? = "不明"
)

data class Location(
    var prefecture: String
)