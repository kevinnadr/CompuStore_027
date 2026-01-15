package com.example.compustore2.repositori

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    // Kunci penyimpanan
    private companion object {
        val IS_LOGIN = booleanPreferencesKey("is_login")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    // Membaca status login
    val isLogin: Flow<Boolean> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            preferences[IS_LOGIN] ?: false
        }

    // Fungsi Simpan Sesi Login
    suspend fun saveLoginSession(isLogin: Boolean, name: String, email: String) {
        dataStore.edit { preferences ->
            preferences[IS_LOGIN] = isLogin
            preferences[USER_NAME] = name
            preferences[USER_EMAIL] = email
        }
    }

    // Fungsi Logout (Hapus sesi)
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}