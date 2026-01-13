package com.example.compustore2.tampilan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.model.DetailTransaksiRequest
import com.example.compustore2.model.TransaksiRequest
import com.example.compustore2.repositori.LocalCartRepository
import com.example.compustore2.repositori.RepositoriCompustore
import kotlinx.coroutines.launch


// State UI Checkout
sealed interface CheckoutUiState {
    object Idle : CheckoutUiState
    object Loading : CheckoutUiState
    object Success : CheckoutUiState
    data class Error(val message: String) : CheckoutUiState
}

class CheckoutViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    var uiState: CheckoutUiState by mutableStateOf(CheckoutUiState.Idle)
        private set

    // Ambil data keranjang & total harga dari Repository Lokal
    val cartItems = LocalCartRepository.cartItems.value
    val totalPrice = LocalCartRepository.getTotalPrice()

    // Input Pilihan User
    var metodePembayaran by mutableStateOf("Transfer") // Default
    var metodePengiriman by mutableStateOf("Diantar")  // Default

    fun processCheckout() {
        uiState = CheckoutUiState.Loading

        val user = repositori.getLoggedInUser()
        if (user == null) {
            uiState = CheckoutUiState.Error("User belum login!")
            return
        }

        // 1. Konversi Item Keranjang ke format DetailTransaksiRequest
        val detailBarang = cartItems.map { item ->
            DetailTransaksiRequest(
                produkId = item.produk.id,
                jumlah = item.jumlah,
                hargaSatuan = item.produk.harga,
                subtotal = item.totalHarga
            )
        }

        // 2. Buat Request Object
        val request = TransaksiRequest(
            userId = user.userId,
            totalHarga = totalPrice,
            metodePembayaran = metodePembayaran,
            metodePengiriman = metodePengiriman,
            detailBarang = detailBarang
        )

        // 3. Kirim ke Backend
        viewModelScope.launch {
            try {
                val response = repositori.createTransaksi(request)
                if (response.isSuccessful) {
                    LocalCartRepository.clearCart() // Kosongkan keranjang jika sukses
                    uiState = CheckoutUiState.Success
                } else {
                    uiState = CheckoutUiState.Error("Gagal Checkout: ${response.message()}")
                }
            } catch (e: Exception) {
                uiState = CheckoutUiState.Error("Terjadi kesalahan jaringan.")
            }
        }
    }
}