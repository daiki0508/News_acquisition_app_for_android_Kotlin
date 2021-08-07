package com.websarva.wings.android.newsapp_kotlin.service

import androidx.annotation.Keep
import com.websarva.wings.android.newsapp_kotlin.model.Translate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface TranslateService {
    @GET("exec")
    fun getRawRequestForTranslate(@QueryMap params: Map<String, String?>): Call<Translate>
}