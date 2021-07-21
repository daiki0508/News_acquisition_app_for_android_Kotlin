package com.websarva.wings.android.newsapp_kotlin.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }
    }
}