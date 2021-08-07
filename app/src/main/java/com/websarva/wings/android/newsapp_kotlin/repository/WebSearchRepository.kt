package com.websarva.wings.android.newsapp_kotlin.repository

import com.websarva.wings.android.newsapp_kotlin.model.Search
import com.websarva.wings.android.newsapp_kotlin.service.SearchService
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLPeerUnverifiedException

class WebSearchRepository: SearchService {
    override fun getRawRequestForSearch(word: String): Call<Search> {
        val serviceSearch: SearchService = CommonRepository().common().create(SearchService::class.java)

        return serviceSearch.getRawRequestForSearch(word)
    }
}