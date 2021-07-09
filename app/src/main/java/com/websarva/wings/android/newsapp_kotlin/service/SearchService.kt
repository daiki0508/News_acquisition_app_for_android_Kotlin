package com.websarva.wings.android.newsapp_kotlin.service

import com.websarva.wings.android.newsapp_kotlin.model.Search
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("websearchAPI.php")
    fun getRawRequestForSearch(@Query("word")word: String): Call<Search>
}