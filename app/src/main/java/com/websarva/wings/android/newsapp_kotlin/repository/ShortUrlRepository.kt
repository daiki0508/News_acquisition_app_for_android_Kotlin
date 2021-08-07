package com.websarva.wings.android.newsapp_kotlin.repository

import com.websarva.wings.android.newsapp_kotlin.model.ShortUrl
import com.websarva.wings.android.newsapp_kotlin.service.SearchService
import com.websarva.wings.android.newsapp_kotlin.service.ShortUrlService
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLPeerUnverifiedException

class ShortUrlRepository: ShortUrlService {
    override fun postRawRequestForShortUrl(words: String): Call<ShortUrl> {
        val serviceSearch: ShortUrlService = CommonRepository().common().create(ShortUrlService::class.java)

        return serviceSearch.postRawRequestForShortUrl(words)
    }
}