package com.example.compustore2.tampilan.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.model.RegisterRequest
import com.example.compustore2.repositori.RepositoriCompustore
import kotlinx.coroutines.launch

sealed interface RegisterUiState {
    object Idle : RegisterUiState
    object Loading : RegisterUiState
    object Success : RegisterUiState
    data class Error(val message: String) : RegisterUiState
}

class RegisterViewModel(private val repositori: RepositoriCompustore) : ViewModel() {
    var uiState: RegisterUiState by mutableStateOf(RegisterUiState.Idle)
        private set

    var nama by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var noHp by mutableStateOf("")

    fun onRegister() {
        uiState = RegisterUiState.Loading
        viewModelScope.launch {
            try {
                val response = repositori.register(RegisterRequest(nama, email, password, noHp))
                if (response.isSuccessful) {
                    uiState = RegisterUiState.Success
                } else {
                    uiState = RegisterUiState.Error("Register gagal: ${response.message()}")
                }
            } catch (e: Exception) {
                uiState = RegisterUiState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    fun resetState() { uiState = RegisterUiState.Idle }
}