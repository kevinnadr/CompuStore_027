package com.example.compustore2.tampilan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.compustore2.model.Produk
import com.example.compustore2.repositori.RepositoriCompustore

// 1. Data Class untuk menampung inputan user (Event)
data class InsertUiEvent(
    val namaProduk: String = "",
    val harga: String = "",
    val stok: String = "",
    val kategori: String = "",
    val deskripsi: String = ""
)

// 2. Konversi inputan String ke Model Produk (Database)
fun InsertUiEvent.toProduk(): Produk = Produk(
    id = 0, // ID otomatis diatur server/database
    namaProduk = namaProduk,
    harga = harga.toDoubleOrNull() ?: 0.0, // Ubah text ke angka
    stok = stok.toIntOrNull() ?: 0,       // Ubah text ke angka
    kategori = kategori,
    deskripsi = deskripsi,
    gambar = "" // Default kosong dulu
)

// 3. State UI Utama
data class EntryUiState(
    val insertUiEvent: InsertUiEvent = InsertUiEvent()
)

class EntryViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    // State yang dipantau oleh UI
    var uiState by mutableStateOf(EntryUiState())
        private set

    // Fungsi untuk update state saat user mengetik
    fun updateUiState(newEntryUiState: EntryUiState) {
        uiState = newEntryUiState.copy() // Validasi bisa ditambahkan di sini
    }

    // Fungsi Simpan ke Database
    suspend fun saveProduk() {
        if (validateInput()) {
            repositori.insertProduk(uiState.insertUiEvent.toProduk())
        }
    }

    // Validasi sederhana (pastikan tidak kosong)
    private fun validateInput(uiEvent: InsertUiEvent = uiState.insertUiEvent): Boolean {
        return uiEvent.namaProduk.isNotBlank() &&
                uiEvent.harga.isNotBlank() &&
                uiEvent.stok.isNotBlank()
    }
}