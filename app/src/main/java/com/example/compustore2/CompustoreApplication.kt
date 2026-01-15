package com.example.compustore2

import android.app.Application
import com.example.compustore2.repositori.AppContainer
import com.example.compustore2.repositori.DefaultAppContainer

class CompustoreApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Kirim 'this' (Context aplikasi) ke Container
        container = DefaultAppContainer(this)
    }
}