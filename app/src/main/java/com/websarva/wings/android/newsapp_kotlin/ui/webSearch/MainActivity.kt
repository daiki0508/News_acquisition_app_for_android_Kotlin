package com.websarva.wings.android.newsapp_kotlin.ui.webSearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SimpleAdapter
import android.widget.Toast
import com.websarva.wings.android.newsapp_kotlin.R
import com.websarva.wings.android.newsapp_kotlin.SearchService
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivityMainBinding
import com.websarva.wings.android.newsapp_kotlin.model.Value
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WebSearchViewModel by viewModel()
    private val retrofit = Retrofit.Builder().apply {
        baseUrl("https://daiki0508-sakura-vps-server.cf/")
            .addConverterFactory(GsonConverterFactory.create())
    }.build()
    private val service = retrofit.create(SearchService::class.java)
    private val from = arrayOf("title", "url")
    private val to = intArrayOf(android.R.id.text1, android.R.id.text2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        binding.progressbar.visibility = View.GONE
        val outputLang = getString(R.string.app_name)

        viewModel.value().observe(this, {
            val value = viewModel.value().value!!
            if (!value.isNullOrEmpty()){
                val resultList: MutableList<MutableMap<String, String>> = mutableListOf()
                Log.d("test", "called!!")
                var result: MutableMap<String,String>
                for (i in 0..9){
                    
                    result = mutableMapOf("title" to value[i].title, "url" to value[i].url)
                    resultList.add(result)
                }

                val adapter = SimpleAdapter(this, resultList, android.R.layout.simple_list_item_2, from, to)
                binding.resultNewsText2.adapter = adapter
                adapter.notifyDataSetChanged()
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
                val get = service.getRawRequestForSearch(viewModel.word().value!!)
                viewModel.receiveSearchDataGet(get)
            }
        }
    }
}