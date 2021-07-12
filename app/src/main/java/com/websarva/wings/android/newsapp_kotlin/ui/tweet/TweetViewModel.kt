package com.websarva.wings.android.newsapp_kotlin.ui.tweet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TweetViewModel: ViewModel() {
    private val _edit = MutableLiveData<String>().apply {
        MutableLiveData<String>()
    }
    private val _limit = MutableLiveData<Int>().apply {
        MutableLiveData<Int>()
    }

    fun setLimit(length: Int){
        _limit.value = length
    }
    fun setData(p0: String){
        _edit.value = p0
    }

    fun edit(): MutableLiveData<String>{
        return _edit
    }
    fun limit(): MutableLiveData<Int>{
        return _limit
    }

    init {
        _edit.value = ""
        _limit.value = 0
    }
}