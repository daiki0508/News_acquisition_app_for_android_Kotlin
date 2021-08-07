package com.websarva.wings.android.newsapp_kotlin.repository

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLPeerUnverifiedException

interface Common{
    fun common(): Retrofit
}

class CommonRepository : Common{
    override fun common(): Retrofit {
        val certificatePinner = CertificatePinner.Builder().apply {
            add("daiki0508-sakura-vps-server.cf","sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=")
        }.build()
        val okHttpClient = OkHttpClient.Builder().apply {
            certificatePinner(certificatePinner)
            hostnameVerifier { s, sslSession ->
                if (!s.equals(sslSession.peerHost)){
                    throw SSLPeerUnverifiedException("Invalid Hostname")
                }
                return@hostnameVerifier true
            }
        }.build()
        return Retrofit.Builder().apply {
            client(okHttpClient)
            baseUrl("https://daiki0508-sakura-vps-server.cf/")
                .addConverterFactory(GsonConverterFactory.create())
        }.build()
    }
}