package com.websarva.wings.android.newsapp_kotlin.ui.webSearch

import android.util.Log
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.android.newsapp_kotlin.TranslateService
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivityMainBinding
import com.websarva.wings.android.newsapp_kotlin.model.Search
import com.websarva.wings.android.newsapp_kotlin.model.Translate
import com.websarva.wings.android.newsapp_kotlin.model.Value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("BlockingMethodInNonBlockingContext")
class WebSearchViewModel: ViewModel() {
    private val retrofitTranslate = Retrofit.Builder().apply {
        baseUrl("https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/")
            .addConverterFactory(GsonConverterFactory.create())
    }.build()
    private val serviceTranslate = retrofitTranslate.create(TranslateService::class.java)

    private val _word = MutableLiveData<String>().apply {
        MutableLiveData<String>()
    }
    private val _value = MutableLiveData<MutableList<Value>>().apply {
        MutableLiveData<MutableList<Value>>()
    }

    @UiThread
    fun receiveSearchDataGet(get: Call<Search>, outputLang: String){
        viewModelScope.launch {
            val responseBody = searchDataBackGroundRunner(get)
            if (responseBody.isSuccessful) {
                responseBody.body()?.let {
                    //_value.value = it.value.toMutableList()

                    Log.d("test", "called!!")
                    val code: String = when (outputLang) {
                        "ニュースアプリ_Kotlin" -> "ja"
                        "新闻应用_Kotlin" -> "zh"
                        "뉴스 애플 리케이션_Kotlin" -> "ko"
                        "Приложение новостей" -> "ru"
                        else -> "en"
                    }
                    if (code != "en") {
                        for (i in 0..9) {
                            val params: Map<String, String> = hashMapOf(
                                "text" to it.value[i].title,
                                "source" to "en",
                                "target" to code
                            )
                            val getTranslate = serviceTranslate.getRawRequestForTranslate(params)
                                val responseBodyTranslate =
                                    translateDataBackGroundRunner(getTranslate)
                                responseBodyTranslate.body()?.let { it2 ->
                                    it.value[i].title = it2.text
                                    Log.d("test", it2.text)
                            }
                        }
                    }
                    _value.postValue(it.value.toMutableList())
                }
            } else {
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

    @WorkerThread
    private suspend fun translateDataBackGroundRunner(get: Call<Translate>): Response<Translate>{
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