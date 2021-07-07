package com.websarva.wings.android.newsapp_kotlin.ui.webSearch

import android.util.Log
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivityMainBinding
import com.websarva.wings.android.newsapp_kotlin.model.Search
import com.websarva.wings.android.newsapp_kotlin.model.Value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

@Suppress("BlockingMethodInNonBlockingContext")
class WebSearchViewModel: ViewModel() {
    private val _word = MutableLiveData<String>().apply {
        MutableLiveData<String>()
    }
    private val _value = MutableLiveData<MutableList<Value>>().apply {
        MutableLiveData<MutableList<Value>>()
    }

    @UiThread
    fun receiveSearchDataGet(get: Call<Search>){
        viewModelScope.launch {
            val responseBody = searchDataBackGroundRunner(get)
            if (responseBody.isSuccessful){
                responseBody.body()?.let {
                    _value.postValue(it.value.toMutableList())
                }
            }else{
                Log.e("test", "Error!!")
            }
        }
    }

    @WorkerThread
    private suspend fun searchDataBackGroundRunner(get: Call<Search>): Response<Search>{
        return withContext(Dispatchers.IO){
            val responseBody = get.execute()

            responseBody
        }
    }

    fun word(): MutableLiveData<String>{
        return _word
    }
    fun value(): MutableLiveData<MutableList<Value>>{
        return _value
    }
    fun setData(binding: ActivityMainBinding){
        _word.value = binding.searchConditions3Edit.text.toString()
    }

    init {
        _word.value = ""
        _value.value = mutableListOf()
    }
}