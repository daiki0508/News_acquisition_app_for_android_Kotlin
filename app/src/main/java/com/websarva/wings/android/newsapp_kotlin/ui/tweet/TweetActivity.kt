package com.websarva.wings.android.newsapp_kotlin.ui.tweet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import com.websarva.wings.android.newsapp_kotlin.R
import com.websarva.wings.android.newsapp_kotlin.databinding.ActivityTweetBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TweetActivity : AppCompatActivity(), TextWatcher {
    private lateinit var binding: ActivityTweetBinding
    private val viewModel: TweetViewModel by viewModel()

    private var maxLength: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTweetBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val uri = intent.getStringExtra("uri")
        Log.d("test", uri.toString())
        maxLength = 140 - uri?.length!!
        val filter = InputFilter.LengthFilter(maxLength)
        binding.editTextShareMessage.filters = arrayOf(filter)
        viewModel.setLimit(maxLength - viewModel.edit().value!!.length)

        binding.editTextShareMessage.addTextChangedListener(this)

        viewModel.limit().observe(this, {
            binding.editLength.text = getString(R.string.limit_text, viewModel.limit().value)
        })
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable) {
        viewModel.setData(p0.toString())
        viewModel.setLimit(maxLength - viewModel.edit().value!!.length)
    }
}