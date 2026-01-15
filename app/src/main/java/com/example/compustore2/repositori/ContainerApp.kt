package com.example.compustore2.repositori

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.compustore2.service_api.CompustoreService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

// 1. Setup DataStore (Nama file penyimpanan di HP)
private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)

// 2. Interface Container (Daftar Menu Wadah)
interface AppContainer {
    val repositoriCompustore: RepositoriCompustore
    val userPreferencesRepository: UserPreferencesRepository // <--- Tambahan Baru
}

// 3. Implementasi Container
class DefaultAppContainer(private val context: Context) : AppContainer {

    private val baseUrl = "http://10.0.2.2:3000/api/" // IP Emulator Android Studio

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: CompustoreService by lazy {
        retrofit.create(CompustoreService::class.java)
    }

    override val repositoriCompustore: RepositoriCompustore by lazy {
        RepositoriCompustore(retrofitService)
    }

    // Inisialisasi UserPreferencesRepository dengan DataStore
    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.dataStore)
    }
}