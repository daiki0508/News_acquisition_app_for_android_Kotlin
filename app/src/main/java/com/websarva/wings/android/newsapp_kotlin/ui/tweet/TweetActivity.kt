package com.websarva.wings.android.newsapp_kotlin.ui.tweet

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.core.text.htmlEncode
import com.google.android.material.snackbar.Snackbar
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

        val url = intent.getStringExtra("url")
        Log.d("test", url.toString())
        maxLength = 140 - url?.length!!
        val filter = InputFilter.LengthFilter(maxLength)
        binding.editTextShareMessage.filters = arrayOf(filter)
        viewModel.setLimit(maxLength - viewModel.edit().value!!.length)

        binding.editTextShareMessage.addTextChangedListener(this)

        viewModel.limit().observe(this, {
            binding.editLength.text = getString(R.string.limit_text, viewModel.limit().value)
        })

        binding.TweetButton.setOnClickListener {
            viewModel.setData(binding.editTextShareMessage.text.toString())

            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("twitter://post?message=${viewModel.edit().value!!.htmlEncode()}$url")
                try {
                    startActivity(this)
                }catch (e: ActivityNotFoundException){
                    Snackbar.make(it, R.string.no_tweet_app, Snackbar.LENGTH_LONG).apply {
                        setAction(R.string.tweet_app_install){
                            Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(getString(R.string.playStore_url))
                                try {
                                    startActivity(this)
                                }catch (e: ActivityNotFoundException){
                                    Log.e("Error", "致命的なエラーの発生")
                                }
                            }
                        }
                        show()
                    }
                }
            }
        }
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