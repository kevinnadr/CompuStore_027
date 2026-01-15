package com.example.compustore2.tampilan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.model.CartItem
import com.example.compustore2.model.Produk
import com.example.compustore2.repositori.RepositoriCompustore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

// UI State (Dimodifikasi agar bisa membawa pesan error)
sealed interface HomeUiState {
    data class Success(val produk: List<Produk>) : HomeUiState
    data class Error(val message: String) : HomeUiState // <--- Simpan pesan error di sini
    object Loading : HomeUiState
}

class HomeViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    // State Utama
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    // State Keranjang (Realtime)
    val cartItems: StateFlow<List<CartItem>> = repositori.cartItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        getProduk()
    }

    // Fungsi Ambil Data dengan Diagnosa Error
    fun getProduk() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            homeUiState = try {
                val listProduk = repositori.getProduk()
                HomeUiState.Success(listProduk)
            } catch (e: IOException) {
                // Error Koneksi (Internet mati / Server mati / Salah IP)
                HomeUiState.Error("Koneksi Gagal: ${e.localizedMessage}. Cek IP & Server.")
            } catch (e: Exception) {
                // Error Lain (Format JSON salah / Data null)
                HomeUiState.Error("Error Data: ${e.localizedMessage}")
            }
        }
    }

    // Fungsi Keranjang
    fun addToCart(produk: Produk) {
        viewModelScope.launch {
            repositori.addToCart(produk)
        }
    }

    fun removeFromCart(produkId: Int) {
        viewModelScope.launch {
            repositori.removeFromCart(produkId)
        }
    }

    fun getTotalCartPrice(): Double {
        return cartItems.value.sumOf { it.produk.harga * it.jumlah }
    }

    // --- Dummy Auth (Agar HalamanHome tidak error) ---
    fun isUserLoggedIn(): Boolean {
        return true // Anggap user sudah login (untuk testing)
    }
}