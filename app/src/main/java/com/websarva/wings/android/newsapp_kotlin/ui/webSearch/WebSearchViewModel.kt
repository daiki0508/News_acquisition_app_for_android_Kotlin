package com.websarva.wings.android.newsapp_kotlin.ui.webSearch

import android.util.Log
import android.widget.ProgressBar
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.android.newsapp_kotlin.service.ShortUrlService
import com.websarva.wings.android.newsapp_kotlin.service.TranslateService
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivityMainBinding
import com.websarva.wings.android.newsapp_kotlin.model.*
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

    private val serviceShortUrl = MainActivity().retrofitSearch.create(ShortUrlService::class.java)

    private val _word = MutableLiveData<String>().apply {
        MutableLiveData<String>()
    }
    private val _value = MutableLiveData<MutableList<Value>>().apply {
        MutableLiveData<MutableList<Value>>()
    }

    @UiThread
    fun receiveSearchDataGet(get: Call<Search>, outputLang: String, progressBar: ProgressBar) {
        viewModelScope.launch {
            val responseBody = searchDataBackGroundRunner(get)
            if (responseBody.isSuccessful) {
                responseBody.body()?.let {
                    //_value.value = it.value.toMutableList()
                    Log.d("test", "called!!")
                    progressBar.progress = 10
                    receiveTranslateData(it.value, outputLang, progressBar)
                }
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

    @UiThread
    private suspend fun receiveTranslateData(value: List<Value>, outputLang: String, progressBar: ProgressBar){
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
                        "text" to value[i].title,
                        "source" to "en",
                        "target" to code
                    )
                    val getTranslate = serviceTranslate.getRawRequestForTranslate(params)
                    val responseBodyTranslate =
                        translateDataBackGroundRunner(getTranslate)
                    val it = responseBodyTranslate.body()
                        value[i].title = it?.text.toString()

                    progressBar.progress += 4
                }
            }else{
                progressBar.progress = 50
            }
            //_value.postValue(value.toMutableList())
            receiveShortUrl(value, progressBar)

    }

    @WorkerThread
    private suspend fun translateDataBackGroundRunner(get: Call<Translate>): Response<Translate>{
        return withContext(Dispatchers.IO){
            val responseBody = get.execute()

            responseBody
        }
    }

    @UiThread
    private suspend fun receiveShortUrl(value: List<Value>, progressBar: ProgressBar){
            for (i in 0..9){
                Log.d("test", "UrlCalled!!")
                val post = serviceShortUrl.postRawRequestForShortUrl(value[i].url)
                Log.d("test2", "UrlCalled!!")
                val responseBody = shortUrlBackGroundRunner(post)
                responseBody.body()?.let {
                    value[i].url = it.data.url
                }
                progressBar.progress += 5
            }
            _value.postValue(value.toMutableList())
    }

    @WorkerThread
    private suspend fun shortUrlBackGroundRunner(post: Call<ShortUrl>): Response<ShortUrl>{
        return withContext(Dispatchers.IO){
            val responseBody = post.execute()

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