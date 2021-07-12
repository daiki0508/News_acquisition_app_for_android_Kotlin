package com.websarva.wings.android.newsapp_kotlin.di

import com.websarva.wings.android.newsapp_kotlin.ui.tweet.TweetViewModel
import com.websarva.wings.android.newsapp_kotlin.ui.webSearch.WebSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module

class MyModule(
    val module: Module = org.koin.dsl.module {
        viewModel { WebSearchViewModel() }
        viewModel { TweetViewModel() }
    }
)