package com.example.compustore2.repositori

import com.example.compustore2.service_api.CompustoreService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val repositoriCompustore: RepositoriCompustore
}

class DefaultAppContainer : AppContainer {

    // GANTI DENGAN IP LAPTOP ANDA (Cek CMD > ipconfig)
    private val baseUrl = "http://10.0.2.2:3000/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: CompustoreService by lazy {
        retrofit.create(CompustoreService::class.java)
    }

    override val repositoriCompustore: RepositoriCompustore by lazy {
        NetworkRepositoriCompustore(retrofitService)
    }
}