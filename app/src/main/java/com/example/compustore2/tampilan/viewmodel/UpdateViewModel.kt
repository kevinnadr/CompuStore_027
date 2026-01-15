package com.example.compustore2.tampilan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.repositori.RepositoriCompustore
import com.example.compustore2.tampilan.route.DestinasiUpdate
import kotlinx.coroutines.launch

class UpdateViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositori: RepositoriCompustore
) : ViewModel() {

    // Ambil ID dari navigasi
    private val _itemId: Int = checkNotNull(savedStateHandle[DestinasiUpdate.itemId])

    var uiState by mutableStateOf(EntryUiState())
        private set

    init {
        getProduk()
    }

    private fun getProduk() {
        viewModelScope.launch {
            val response = repositori.getProdukById(_itemId)
            if (response.isSuccessful && response.body() != null) {
                val produk = response.body()!!

                // PERBAIKAN DI SINI:
                // Tambahkan ?: "" untuk menangani data null
                uiState = EntryUiState(
                    insertUiEvent = InsertUiEvent(
                        namaProduk = produk.namaProduk ?: "", // Jika null, ganti jadi text kosong
                        harga = produk.harga.toString(),
                        stok = produk.stok.toString(),
                        kategori = produk.kategori ?: "",    // Jika null, ganti jadi text kosong
                        deskripsi = produk.deskripsi ?: ""
                    )
                )
            }
        }
    }

    fun updateUiState(newState: EntryUiState) {
        uiState = newState
    }

    fun updateProduk() {
        viewModelScope.launch {
            val produkUpdate = uiState.insertUiEvent.toProduk().copy(id = _itemId)
            repositori.updateProduk(_itemId, produkUpdate)
        }
    }
}