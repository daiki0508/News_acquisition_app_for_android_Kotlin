package com.websarva.wings.android.newsapp_kotlin.repository

import com.websarva.wings.android.newsapp_kotlin.model.Search
import com.websarva.wings.android.newsapp_kotlin.service.SearchService
import retrofit2.Call

class WebSearchRepository: SearchService {
    override fun getRawRequestForSearch(word: String): Call<Search> {
        val serviceSearch: SearchService = CommonRepository().common().create(SearchService::class.java)

        return serviceSearch.getRawRequestForSearch(word)
    }
}