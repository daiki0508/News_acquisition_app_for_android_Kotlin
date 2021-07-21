package com.websarva.wings.android.newsapp_kotlin.ui.license

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivityLicenseBinding

class LicenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLicenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLicenseBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val webview = binding.webview
        webview.let {
            it.settings.allowFileAccess = false
            it.settings.allowContentAccess = false
            it.settings.javaScriptEnabled = false

            it.loadUrl("file:///android_asset/licenses.html")
        }
    }
}