package com.example.compustore2.tampilan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.model.Produk
import com.example.compustore2.repositori.RepositoriCompustore
import kotlinx.coroutines.launch

// State untuk Form Input
data class DetailProdukUiState(
    val nama: String = "",
    val kategori: String = "",
    val merk: String = "",
    val harga: String = "",
    val stok: String = "",
    val deskripsi: String = "",
    val gambar: String = "" // Nanti diisi nama file gambar
)

class EntryViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    var uiState by mutableStateOf(DetailProdukUiState())
        private set

    // Fungsi update state saat user mengetik
    fun updateUiState(detailProduk: DetailProdukUiState) {
        uiState = detailProduk
    }

    // Fungsi Simpan ke Database
    fun saveProduk() {
        viewModelScope.launch {
            // Konversi UI State ke Model Produk
            // ID dikasih 0 karena Auto Increment di MySQL
            val produk = Produk(
                id = 0,
                namaProduk =  uiState.nama,
                kategori = uiState.kategori,
                merk = uiState.merk,
                harga = uiState.harga.toDoubleOrNull() ?: 0.0,
                stok = uiState.stok.toIntOrNull() ?: 0,
                deskripsi = uiState.deskripsi,
                gambar = "laptop_asus.jpg" // HARDCODE DULU (Nanti kita bahas upload gambar)
            )

            repositori.insertProduk(produk)
        }
    }
}