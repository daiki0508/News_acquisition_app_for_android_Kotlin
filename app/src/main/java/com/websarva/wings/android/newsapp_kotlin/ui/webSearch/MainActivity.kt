package com.websarva.wings.android.newsapp_kotlin.ui.webSearch

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.websarva.wings.android.newsapp_kotlin.DialogLister
import com.websarva.wings.android.newsapp_kotlin.R
import com.websarva.wings.android.newsapp_kotlin.service.SearchService
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivityMainBinding
import com.websarva.wings.android.newsapp_kotlin.service.TranslateService
import com.websarva.wings.android.newsapp_kotlin.ui.tweet.TweetActivity
import com.websarva.wings.android.newsapp_kotlin.ui.weather.WeatherActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), DialogLister {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WebSearchViewModel by viewModel()

    val retrofitSearch: Retrofit = Retrofit.Builder().apply {
        baseUrl("https://daiki0508-sakura-vps-server.cf/")
            .addConverterFactory(GsonConverterFactory.create())
    }.build()
    private val serviceSearch = retrofitSearch.create(SearchService::class.java)

    private val retrofitTranslate: Retrofit = Retrofit.Builder().apply {
        baseUrl("https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/")
            .addConverterFactory(GsonConverterFactory.create())
    }.build()
    val serviceTranslate: TranslateService = retrofitTranslate.create(TranslateService::class.java)

    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val progressBar = binding.progressbar
        progressBar.visibility = View.GONE
        progressBar.max = 100

        val outputLang = getString(R.string.app_name)

        val from = arrayOf("title", "url")
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        viewModel.value().observe(this, {
            val value = viewModel.value().value!!
            if (!value.isNullOrEmpty()){
                val resultList: MutableList<MutableMap<String, String>> = mutableListOf()
                var result: MutableMap<String, String>
                for (i in 0..9){
                    result = mutableMapOf("title" to it[i].title, "url" to it[i].url)
                    resultList.add(result)
                }

                val adapter = SimpleAdapter(this, resultList, android.R.layout.simple_list_item_2, from, to)
                binding.resultNewsText2.adapter = adapter
                adapter.notifyDataSetChanged()

                progressBar.visibility = View.GONE
                binding.executeButton.isEnabled = true
            }
        })

        binding.executeButton.setOnClickListener {
            viewModel.setData(binding)
            Log.d("test", viewModel.word().value!!)
            if (viewModel.word().value.isNullOrBlank()){
                val show: String = when(outputLang){
                    "NewsApp_Kotlin" -> "No search term entered"
                    "新闻应用_Kotlin" -> "没有输入搜索词"
                    "뉴스 애플 리케이션_Kotlin" -> "검색어가 입력되어 있지 않습니다"
                    "Приложение новостей" -> "Поисковый запрос не введен"
                    else -> "検索用語が入力されていません"
                }
                Toast.makeText(this, show, Toast.LENGTH_SHORT).show()
            }else{
                binding.executeButton.isEnabled = false
                progressBar.visibility = View.VISIBLE
                progressBar.progress = 0

                val get = serviceSearch.getRawRequestForSearch(viewModel.word().value!!)
                viewModel.receiveSearchDataGet(get, outputLang, progressBar)
            }
        }

        binding.resultNewsText2.setOnItemClickListener { adapterView, _, i, _ ->
            val item = adapterView.getItemAtPosition(i) as MutableMap<*, *>
            url = item["url"] as String

            val selectDialogFragment = SelectDialog()
            selectDialogFragment.show(supportFragmentManager, "selectFragment")
        }

        binding.tvWeather.setOnClickListener {
            startActivity(Intent(this, WeatherActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onDialogFlagReceive(dialog: DialogFragment, flag: Boolean) {
        if (flag){
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }else{
            Intent(this, TweetActivity::class.java).apply {
                putExtra("url", url)
                startActivity(this)
            }
        }
    }
}