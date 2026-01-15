package com.example.compustore2.tampilan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.repositori.RepositoriCompustore
import com.example.compustore2.repositori.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// State UI untuk Profile
data class ProfileUiState(
    val isLogin: Boolean = false,
    val username: String = "",
    val email: String = ""
)

class ProfileViewModel(
    private val repositori: RepositoriCompustore,
    // INI YANG SEBELUMNYA KURANG:
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // Mengambil data user secara realtime dari penyimpanan lokal
    val uiState: StateFlow<ProfileUiState> = userPreferencesRepository.isLogin
        .map { isLoginStatus ->
            ProfileUiState(
                isLogin = isLoginStatus,
                username = "User", // Nanti bisa diambil dari preferences juga jika disimpan
                email = "user@example.com"
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState()
        )

    fun logout() {
        viewModelScope.launch {
            userPreferencesRepository.logout()
        }
    }
}