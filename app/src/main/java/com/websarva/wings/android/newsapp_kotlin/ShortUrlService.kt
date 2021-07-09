package com.websarva.wings.android.newsapp_kotlin

import com.websarva.wings.android.newsapp_kotlin.model.ShortUrl
import retrofit2.Call
import retrofit2.http.*

interface ShortUrlService {
    @FormUrlEncoded
    @POST("bitlyAPI.php")
    fun postRawRequestForShortUrl(@Field("words")words: String): Call<ShortUrl>
}