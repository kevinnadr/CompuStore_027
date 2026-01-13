package com.example.compustore2.tampilan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.model.LoginRequest
import com.example.compustore2.model.User
import com.example.compustore2.repositori.RepositoriCompustore
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// State untuk UI (Loading, Sukses, Gagal)
sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val user: User) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    var uiState: LoginUiState by mutableStateOf(LoginUiState.Idle)
        private set

    // State untuk input form
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    fun onLogin() {
        uiState = LoginUiState.Loading
        viewModelScope.launch {
            try {
                val response = repositori.login(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.data != null) {
                        uiState = LoginUiState.Success(body.data)
                    } else {
                        uiState = LoginUiState.Error("Respon server kosong")
                    }
                } else {
                    uiState = LoginUiState.Error("Login gagal: ${response.message()}")
                }
            } catch (e: IOException) {
                uiState = LoginUiState.Error("Kesalahan jaringan. Periksa koneksi internet.")
            } catch (e: HttpException) {
                uiState = LoginUiState.Error("Kesalahan server: ${e.message}")
            }
        }
    }

    // Reset status jika ingin login ulang setelah error
    fun resetState() {
        uiState = LoginUiState.Idle
    }
}