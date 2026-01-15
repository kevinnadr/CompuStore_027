package com.example.compustore2.tampilan.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.model.RiwayatTransaksi
import com.example.compustore2.repositori.RepositoriCompustore
import kotlinx.coroutines.launch
import java.io.IOException

// Status UI: Loading, Sukses (bawa data), atau Error
sealed interface RiwayatUiState {
    object Loading : RiwayatUiState
    data class Success(val riwayat: List<RiwayatTransaksi>) : RiwayatUiState
    object Error : RiwayatUiState
}

class RiwayatViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    var uiState: RiwayatUiState by mutableStateOf(RiwayatUiState.Loading)
        private set

    init {
        getRiwayat()
    }

    // Fungsi ambil data real
    fun getRiwayat() {
        viewModelScope.launch {
            uiState = RiwayatUiState.Loading
            try {
                val listRiwayat = repositori.getRiwayatTransaksi()
                uiState = RiwayatUiState.Success(listRiwayat)
            } catch (e: Exception) {
                // INI PENTING: Cetak error ke Logcat agar ketahuan penyebabnya
                Log.e("RiwayatViewModel", "Error mengambil data: ${e.message}")
                e.printStackTrace()
                uiState = RiwayatUiState.Error
            }
        }
    }
}