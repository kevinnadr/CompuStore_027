package com.example.compustore2.tampilan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.model.CartItem
import com.example.compustore2.model.DetailTransaksiRequest
import com.example.compustore2.model.TransaksiRequest
import com.example.compustore2.repositori.RepositoriCompustore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class CheckoutUiState {
    object Initial : CheckoutUiState()
    object Loading : CheckoutUiState()
    object Success : CheckoutUiState()
    data class Error(val message: String) : CheckoutUiState()
}

class CheckoutViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = repositori.cartItems
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    private val _checkoutState = MutableStateFlow<CheckoutUiState>(CheckoutUiState.Initial)
    val checkoutState: StateFlow<CheckoutUiState> = _checkoutState

    // --- TAMBAHAN 1: VARIABEL UNTUK MENYIMPAN PILIHAN METODE BAYAR ---
    var metodePembayaranDipilih by mutableStateOf("Transfer Bank") // Default
        private set

    fun updateMetodePembayaran(metode: String) {
        metodePembayaranDipilih = metode
    }
    // -----------------------------------------------------------------

    fun prosesCheckout(totalHarga: Double) {
        viewModelScope.launch {
            _checkoutState.value = CheckoutUiState.Loading

            try {
                val currentItems = repositori.cartItems.first()
                if (currentItems.isEmpty()) {
                    _checkoutState.value = CheckoutUiState.Error("Keranjang kosong!")
                    return@launch
                }

                val userId = 1
                val tanggalSekarang = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val detailList = currentItems.map { item ->
                    DetailTransaksiRequest(
                        produkId = item.produk.id,
                        jumlah = item.jumlah,
                        harga = (item.produk.harga * item.jumlah)
                    )
                }

                val request = TransaksiRequest(
                    userId = userId,
                    total = totalHarga,
                    status = "pending",
                    tanggal = tanggalSekarang,
                    alamat = "Jl. Default No. 1",

                    // --- TAMBAHAN 2: GUNAKAN VARIABEL PILIHAN USER ---
                    metodePembayaran = metodePembayaranDipilih,
                    // -------------------------------------------------

                    items = detailList
                )

                val response = repositori.createTransaksi(request)

                if (response.isSuccessful) {
                    repositori.clearCart()
                    _checkoutState.value = CheckoutUiState.Success
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    _checkoutState.value = CheckoutUiState.Error("Gagal: $errorBody")
                }

            } catch (e: Exception) {
                _checkoutState.value = CheckoutUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    fun resetState() {
        _checkoutState.value = CheckoutUiState.Initial
    }
}