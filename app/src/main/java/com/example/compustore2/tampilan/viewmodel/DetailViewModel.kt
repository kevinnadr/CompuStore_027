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

// UI State untuk Halaman Detail
sealed interface DetailUiState {
    data class Success(val produk: Produk) : DetailUiState
    data class Error(val message: String) : DetailUiState
    object Loading : DetailUiState
}

class DetailViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    // Variable State yang dicari oleh HalamanDetail
    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    // 1. Fungsi Ambil Data (Yang dicari error 'getProdukById')
    fun getProdukById(id: Int) {
        viewModelScope.launch {
            detailUiState = DetailUiState.Loading
            try {
                // Validasi ID
                if (id == 0) {
                    detailUiState = DetailUiState.Error("ID Produk Invalid (0).")
                    return@launch
                }

                val response = repositori.getProdukById(id)

                if (response.isSuccessful && response.body() != null) {
                    detailUiState = DetailUiState.Success(response.body()!!)
                } else {
                    detailUiState = DetailUiState.Error("Gagal: Code ${response.code()} (${response.message()})")
                }
            } catch (e: IOException) {
                detailUiState = DetailUiState.Error("Koneksi Error: Cek internet/server.")
            } catch (e: Exception) {
                detailUiState = DetailUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    // 2. Fungsi Tambah Keranjang (Yang dicari error 'addToCart')
    fun addToCart(produk: Produk) {
        viewModelScope.launch {
            repositori.addToCart(produk)
        }
    }

    // 3. Fungsi Hapus Produk
    fun deleteProduk(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = repositori.deleteProduk(id)
                if (response.isSuccessful) {
                    onSuccess()
                }
            } catch (e: Exception) {
                // Handle error silent
            }
        }
    }
}