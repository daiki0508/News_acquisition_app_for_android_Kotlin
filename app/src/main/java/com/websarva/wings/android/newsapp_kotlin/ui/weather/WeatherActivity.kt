package com.websarva.wings.android.newsapp_kotlin.ui.weather

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.websarva.wings.android.newsapp_kotlin.CommonClass
import com.websarva.wings.android.newsapp_kotlin.R
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivityWeatherBinding
import com.websarva.wings.android.newsapp_kotlin.ui.webSearch.MainActivity
import com.websarva.wings.android.newsapp_kotlin.ui.weather.recyclerView.RecyclerViewAdapter
import com.websarva.wings.android.newsapp_kotlin.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private val viewModel: WeatherViewModel by viewModel()

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

        binding = ActivityWeatherBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout,binding.toolbar, R.string.drawer_open, R.string.drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_webSearch -> webSearchIntent()
                R.id.menu_settings -> CommonClass(null).settingsIntent(this)
                R.id.menu_licenses -> CommonClass(null).licenseIntent(this)
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        binding.weatherList.layoutManager = LinearLayoutManager(this)

        val progressBar = binding.progressbar
        progressBar.visibility = View.GONE
        progressBar.max = 100

        var bitMap: Bitmap
        viewModel.weatherList().observe(this, {
            if (!viewModel.weatherList().value.isNullOrEmpty()){
                bitMap = when(viewModel.imageFlag().value){
                    0 -> BitmapFactory.decodeResource(resources, R.drawable.sunny)
                    1 -> BitmapFactory.decodeResource(resources, R.drawable.cloudy)
                    else -> BitmapFactory.decodeResource(resources, R.drawable.rainny)
                }
                binding.ivWeather.setImageBitmap(bitMap)

                val adapter = RecyclerViewAdapter(viewModel.weatherList().value!!, this)
                binding.weatherList.adapter = adapter
                binding.weatherList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
                adapter.notifyDataSetChanged()
                Log.d("test", "Called!!")

                progressBar.progress = 100
                progressBar.visibility = View.GONE
                binding.weatherExecute.isEnabled = true
            }
        })

        binding.weatherExecute.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            binding.weatherExecute.isEnabled = false

            val areaCode = cityCode(binding.searchConditions1List.selectedItemId)
            Log.d("test", areaCode)

            viewModel.receiveWeatherDataGet(areaCode,outputLang = getString(R.string.app_name), progressBar)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val weather = menu?.findItem(R.id.action_weather)
        weather?.isEnabled = false

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_options_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        when(item.itemId){
            R.id.action_webSearch -> webSearchIntent()
            else -> returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }

    private fun webSearchIntent(){
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun cityCode(selected_id: Long): String {
        return when (selected_id.toInt()) {
            in 1..8 -> {
                "0${selected_id + 1}0010"
            }
            in 9..18 -> {
                "${selected_id + 1}0010"
            }
            in 19..28 -> {
                "${selected_id + 1}0010"
            }
            in 29..38 -> {
                "${selected_id + 1}0010"
            }
            in 39..45 -> {
                "${selected_id + 1}0010"
            }
            46 -> {
                "471010"
            }
            else -> {
                "016010"
            }
        }
    }
}