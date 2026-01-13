package com.example.compustore2.tampilan.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.model.RiwayatTransaksi
import com.example.compustore2.repositori.RepositoriCompustore
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface RiwayatUiState {
    data class Success(val riwayat: List<RiwayatTransaksi>) : RiwayatUiState
    object Error : RiwayatUiState
    object Loading : RiwayatUiState
}

class RiwayatViewModel(private val repositori: RepositoriCompustore) : ViewModel() {
    var uiState: RiwayatUiState by mutableStateOf(RiwayatUiState.Loading)
        private set

    init {
        getRiwayat()
    }

    fun getRiwayat() {
        viewModelScope.launch {
            uiState = RiwayatUiState.Loading
            val user = repositori.getLoggedInUser()

            if (user != null) {
                try {
                    val data = repositori.getRiwayatTransaksi(user.userId)
                    uiState = RiwayatUiState.Success(data)
                } catch (e: IOException) {
                    uiState = RiwayatUiState.Error
                } catch (e: Exception) {
                    uiState = RiwayatUiState.Error
                }
            } else {
                uiState = RiwayatUiState.Error // User belum login
            }
        }
    }
}