package com.websarva.wings.android.newsapp_kotlin.repository

import com.websarva.wings.android.newsapp_kotlin.model.ShortUrl
import com.websarva.wings.android.newsapp_kotlin.service.ShortUrlService
import retrofit2.Call

class ShortUrlRepository: ShortUrlService {
    override fun postRawRequestForShortUrl(words: String): Call<ShortUrl> {
        val serviceSearch: ShortUrlService = CommonRepository().common().create(ShortUrlService::class.java)

        return serviceSearch.postRawRequestForShortUrl(words)
    }
}