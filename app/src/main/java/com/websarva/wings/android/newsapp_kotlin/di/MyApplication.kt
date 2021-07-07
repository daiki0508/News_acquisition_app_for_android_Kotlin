package com.websarva.wings.android.newsapp_kotlin.di

import android.app.Application
import com.websarva.wings.android.newsapp_kotlin.di.MyModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(MyModule().module)
        }
    }
}