package com.example.compustore2.tampilan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.model.RegisterRequest
import com.example.compustore2.repositori.RepositoriCompustore
import kotlinx.coroutines.launch

// 1. Definisikan State UI untuk Register di sini agar jelas
data class RegisterUiState(
    val nama: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    // 2. State Holder
    var uiState by mutableStateOf(RegisterUiState())
        private set

    // 3. Fungsi untuk Update Text saat diketik
    fun updateUiState(nama: String? = null, email: String? = null, password: String? = null) {
        uiState = uiState.copy(
            nama = nama ?: uiState.nama,
            email = email ?: uiState.email,
            password = password ?: uiState.password,
            errorMessage = null // Reset error saat user mengetik
        )
    }

    // 4. Fungsi Register ke Server
    fun onRegister() {
        viewModelScope.launch {
            // Validasi Input Kosong
            if (uiState.nama.isBlank() || uiState.email.isBlank() || uiState.password.isBlank()) {
                uiState = uiState.copy(errorMessage = "Semua kolom wajib diisi!")
                return@launch
            }

            uiState = uiState.copy(isLoading = true, errorMessage = null)

            try {
                // PERBAIKAN UTAMA: Gunakan Named Arguments agar tidak tertukar/error constructor
                val request = RegisterRequest(
                    nama = uiState.nama,
                    email = uiState.email,
                    password = uiState.password
                )

                val response = repositori.register(request)

                if (response.isSuccessful) {
                    uiState = uiState.copy(isLoading = false, isSuccess = true)
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Gagal Register: Kode ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error Koneksi: ${e.localizedMessage}"
                )
            }
        }
    }

    // Reset state setelah sukses (agar pas balik ga sukses terus)
    fun resetState() {
        uiState = RegisterUiState()
    }
}