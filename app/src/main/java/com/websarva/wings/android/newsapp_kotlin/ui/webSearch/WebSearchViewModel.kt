package com.websarva.wings.android.newsapp_kotlin.ui.webSearch

import android.annotation.SuppressLint
import android.text.Editable
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.websarva.wings.android.newsapp_kotlin.CommonClass
import com.websarva.wings.android.newsapp_kotlin.R
import com.websarva.wings.android.newsapp_kotlin.service.ShortUrlService
import com.websarva.wings.android.newsapp_kotlin.service.TranslateService
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivityMainBinding
import com.websarva.wings.android.newsapp_kotlin.model.*
import com.websarva.wings.android.newsapp_kotlin.repository.ShortUrlRepository
import com.websarva.wings.android.newsapp_kotlin.repository.TranslateRepository
import com.websarva.wings.android.newsapp_kotlin.repository.WebSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("BlockingMethodInNonBlockingContext")
class WebSearchViewModel(
    private val translateRepository: TranslateRepository,
    private val webSearchRepository: WebSearchRepository,
    private val shortUrlRepository: ShortUrlRepository
): ViewModel() {
    private val _value = MutableLiveData<MutableList<Value>>().apply {
        MutableLiveData<MutableList<Value>>()
    }

    @UiThread
    fun receiveTransDataWordsGet(params: Map<String, String?>, activity: MainActivity, code: String, progressBar: ProgressBar){
        val get = translateRepository.getRawRequestForTranslate(params)
        viewModelScope.launch {
            val responseBody = translateDataWordsBackGroundRunner(get)
            responseBody.body()?.let {
                //Log.d("test", it.text)
                val getNext = webSearchRepository.getRawRequestForSearch(it.text)
                receiveSearchDataGet(getNext, activity, code, progressBar, null)
            }
        }
    }

    @WorkerThread
    private suspend fun translateDataWordsBackGroundRunner(get: Call<Translate>): Response<Translate>{
        return withContext(Dispatchers.IO){
            val responseBody = get.execute()

            responseBody
        }
    }

    @UiThread
    fun receiveSearchDataGet(get: Call<Search>?, activity: MainActivity, code: String, progressBar: ProgressBar, edWord: Editable?) {
        var responseBody: Response<Search>?
        viewModelScope.launch {
            responseBody = if (get == null){
                val get2 =  webSearchRepository.getRawRequestForSearch(edWord.toString())
                edWord?.clear()
                searchDataBackGroundRunner(get2)
            }else{
                searchDataBackGroundRunner(get)
            }
            if (responseBody!!.isSuccessful) {
                responseBody!!.body()?.let {
                    //_value.value = it.value.toMutableList()
                    Log.d("test", "called!!")
                    //Log.d("test", it.value.toString())
                    if (!it.value.isNullOrEmpty()){
                        progressBar.progress = 10
                        receiveTranslateData(it.value, code, progressBar)
                    }else{
                        Toast.makeText(activity, activity.getString(R.string.failure_text), Toast.LENGTH_LONG).show()
                    }
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
    private suspend fun receiveTranslateData(value: List<Value>, code: String, progressBar: ProgressBar){
            if (code != "en") {
                for (i in 0..9) {
                    val params: Map<String, String> = hashMapOf(
                        "text" to value[i].title,
                        "source" to "en",
                        "target" to code
                    )
                    val getTranslate = translateRepository.getRawRequestForTranslate(params)
                    val responseBodyTranslate =
                        translateDataBackGroundRunner(getTranslate)
                    responseBodyTranslate.body()?.let {
                        value[i].title = it.text
                    }
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
                val post = shortUrlRepository.postRawRequestForShortUrl(value[i].url)
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

    fun value(): MutableLiveData<MutableList<Value>>{
        return _value
    }

    init {
        _value.value = mutableListOf()
    }
}