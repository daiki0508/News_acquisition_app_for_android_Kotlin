package com.websarva.wings.android.newsapp_kotlin.ui.webSearch

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.newsapp_kotlin.*
import com.websarva.wings.android.newsapp_kotlin.service.SearchService
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivityMainBinding
import com.websarva.wings.android.newsapp_kotlin.service.TranslateService
import com.websarva.wings.android.newsapp_kotlin.ui.tweet.TweetActivity
import com.websarva.wings.android.newsapp_kotlin.ui.weather.WeatherActivity
import com.websarva.wings.android.newsapp_kotlin.ui.webSearch.recyclerView.RecyclerViewAdapter
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.SSLPeerUnverifiedException

class MainActivity : AppCompatActivity(), DialogLister {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WebSearchViewModel by viewModel()

    private val certificatePinner = CertificatePinner.Builder().apply {
        add("daiki0508-sakura-vps-server.cf","sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=")
    }.build()
    private val okHttpClient = OkHttpClient.Builder().apply {
        certificatePinner(certificatePinner)
        hostnameVerifier { s, sslSession ->
            if (!s.equals(sslSession.peerHost)){
                throw SSLPeerUnverifiedException("Invalid Hostname")
            }
            return@hostnameVerifier true
        }
    }.build()
    val retrofitSearch: Retrofit = Retrofit.Builder().apply {
        client(okHttpClient)
        baseUrl("https://daiki0508-sakura-vps-server.cf/")
            .addConverterFactory(GsonConverterFactory.create())
    }.build()
    val serviceSearch = retrofitSearch.create(SearchService::class.java)

    private lateinit var url: String

    override fun onStart() {
        super.onStart()

        // NightMode
        val mUiManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        var systemThemeMode = false
        if (mUiManager.nightMode == UiModeManager.MODE_NIGHT_YES){
            systemThemeMode = true
        }
        val preference = PreferenceManager.getDefaultSharedPreferences(this)
        if (preference.getBoolean("nightPreference", systemThemeMode)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val security = SecurityCheckClass()

        /*if (security.rootCheck()
            or security.checkRunningProcess(this)
            or security.isDebuggable(this)
            or security.detectDebugger()
            or security.isTestKeyBuild()
            or security.detectThreadCpuTimeNanos()
            ){
            Log.d("test", "Root")
            SecurityDialogFragment().show(supportFragmentManager, "securityFragment")
        }*/

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout,binding.toolbar, R.string.drawer_open, R.string.drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_weather -> weatherIntent()
                R.id.menu_settings -> CommonClass(null).settingsIntent(this)
                R.id.menu_licenses -> CommonClass(null).licenseIntent(this)
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        val progressBar = binding.progressbar
        progressBar.visibility = View.GONE
        progressBar.max = 100
        val outputLang = getString(R.string.app_name)

        binding.resultNewsText2.layoutManager = LinearLayoutManager(this)

        viewModel.value().observe(this, {
            val value = viewModel.value().value!!
            if (!value.isNullOrEmpty()){
                val resultList: MutableList<MutableMap<String, String>> = mutableListOf()
                var result: MutableMap<String, String>
                for (i in 0..9){
                    result = mutableMapOf("title" to it[i].title, "url" to it[i].url, "img" to it[i].image.url!!)
                    resultList.add(result)
                }

                val adapter = RecyclerViewAdapter(resultList)
                binding.resultNewsText2.adapter = adapter
                binding.resultNewsText2.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                adapter.notifyDataSetChanged()

                adapter.setOnItemClickListener(object: OnItemClickListener{
                    override fun onItemClickListener(
                        view: View,
                        position: Int,
                        clickedText: String
                    ) {
                        url = clickedText

                        val selectDialogFragment = SelectDialog()
                        selectDialogFragment.show(supportFragmentManager, "selectFragment")
                    }
                })

                progressBar.visibility = View.GONE
                binding.executeButton.isEnabled = true
            }
        })

        binding.executeButton.setOnClickListener {
            val edWord = Editable.Factory.getInstance().newEditable(binding.searchConditions3Edit.text)
            if (edWord.toString().isBlank()){
                val show: String = when(outputLang){
                    "NewsApp" -> "No search term entered"
                    "新闻应用" -> "没有输入搜索词"
                    "뉴스 애플 리케이션" -> "검색어가 입력되어 있지 않습니다"
                    "Приложение новостей" -> "Поисковый запрос не введен"
                    else -> "検索用語が入力されていません"
                }
                Toast.makeText(this, show, Toast.LENGTH_SHORT).show()
            }else{
                binding.executeButton.isEnabled = false
                progressBar.visibility = View.VISIBLE
                progressBar.progress = 0

                val code = CommonClass(outputLang).code
                if (code != "en"){
                    val params: Map<String, String> = hashMapOf(
                        "text" to edWord.toString(),
                        "source" to code,
                        "target" to "en"
                    )
                    val get = CommonClass(null).serviceTranslate.getRawRequestForTranslate(params)
                    viewModel.receiveTransDataWordsGet(get, this, code, progressBar)
                }else{
                    val get = serviceSearch.getRawRequestForSearch(edWord.toString())
                    viewModel.receiveSearchDataGet(get, this, code, progressBar)
                }
            }
            edWord.clear()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val webSearch = menu?.findItem(R.id.action_webSearch)
        webSearch?.isEnabled = false

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_options_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        when(item.itemId){
            R.id.action_weather -> weatherIntent()
            else -> returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }

    override fun onDialogFlagReceive(dialog: DialogFragment, flag: Boolean) {
        if (flag){
            if (!URL(url).protocol.equals("https")){
                throw MalformedURLException("Invalid Protocol")
            }
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }else{
            Intent(this, TweetActivity::class.java).apply {
                putExtra("url", url)
                startActivity(this)
            }
        }
    }

    private fun weatherIntent(){
        startActivity(Intent(this, WeatherActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}