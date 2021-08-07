package com.websarva.wings.android.newsapp_kotlin.di

import com.websarva.wings.android.newsapp_kotlin.repository.ShortUrlRepository
import com.websarva.wings.android.newsapp_kotlin.repository.TranslateRepository
import com.websarva.wings.android.newsapp_kotlin.repository.WeatherRepository
import com.websarva.wings.android.newsapp_kotlin.repository.WebSearchRepository
import com.websarva.wings.android.newsapp_kotlin.service.ShortUrlService
import com.websarva.wings.android.newsapp_kotlin.ui.tweet.TweetViewModel
import com.websarva.wings.android.newsapp_kotlin.ui.weather.WeatherViewModel
import com.websarva.wings.android.newsapp_kotlin.ui.webSearch.WebSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module

class MyModule(
    val module: Module = org.koin.dsl.module {
        viewModel { WebSearchViewModel(get(), get(), get()) }
        viewModel { TweetViewModel() }
        viewModel { WeatherViewModel(get(), get()) }
    },
    val repository: Module = org.koin.dsl.module {
        factory { ShortUrlRepository() }
        factory { TranslateRepository() }
        factory { WebSearchRepository() }
        factory { WeatherRepository() }
    }
)