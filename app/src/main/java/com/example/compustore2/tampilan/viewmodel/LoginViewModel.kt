package com.example.compustore2.tampilan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.repositori.RepositoriCompustore
import com.example.compustore2.repositori.UserPreferencesRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repositori: RepositoriCompustore,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // --- PASTIKAN TIDAK ADA KATA 'private' DI DEPAN var INI ---
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    fun onLogin(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // Simulasi Login (Nanti diganti API)
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    userPreferencesRepository.saveLoginSession(true, "User", email)
                    onSuccess()
                } else {
                    errorMessage = "Email dan Password tidak boleh kosong"
                }
            } catch (e: Exception) {
                errorMessage = "Login Gagal: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}