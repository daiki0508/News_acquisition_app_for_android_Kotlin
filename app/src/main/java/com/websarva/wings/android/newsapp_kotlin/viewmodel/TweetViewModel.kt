package com.websarva.wings.android.newsapp_kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TweetViewModel: ViewModel() {
    private val _limit = MutableLiveData<Int>().apply {
        MutableLiveData<Int>()
    }

    fun setLimit(length: Int){
        _limit.value = length
    }
    fun limit(): MutableLiveData<Int>{
        return _limit
    }

    init {
        _limit.value = 0
    }
}