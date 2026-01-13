package com.example.compustore2.tampilan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.model.Produk
import com.example.compustore2.repositori.RepositoriCompustore
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val produk: Produk) : DetailUiState
    object Error : DetailUiState
}

class DetailViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    fun getProdukById(id: Int) {
        viewModelScope.launch {
            detailUiState = DetailUiState.Loading
            try {
                val response = repositori.getProdukById(id)

                if (response.isSuccessful && response.body() != null) {
                    // Sukses mengambil data
                    detailUiState = DetailUiState.Success(response.body()!!)
                } else {
                    // Gagal dari Server (Misal 404 atau 500)
                    println("DEBUG_ERROR: Gagal mengambil data. Code: ${response.code()}, Message: ${response.message()}")
                    detailUiState = DetailUiState.Error
                }
            } catch (e: IOException) {
                // Masalah Koneksi (Internet mati / Server mati)
                println("DEBUG_ERROR: Masalah Koneksi: ${e.message}")
                detailUiState = DetailUiState.Error
            } catch (e: Exception) {
                // Error Lainnya (Parsing JSON salah, dll)
                println("DEBUG_ERROR: Terjadi Crash: ${e.message}")
                e.printStackTrace()
                detailUiState = DetailUiState.Error
            }
        }
    }

    fun deleteProduk(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                repositori.deleteProduk(id)
                onSuccess()
            } catch (e: Exception) {
                println("DEBUG_ERROR: Gagal Hapus: ${e.message}")
            }
        }
    }
}