package com.example.compustore2.tampilan.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compustore2.model.CartItem
import com.example.compustore2.model.Produk
import com.example.compustore2.repositori.LocalCartRepository
import com.example.compustore2.repositori.RepositoriCompustore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


// --- STATE MANAGEMENT ---
sealed interface HomeUiState {
    data class Success(val produk: List<Produk>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    // State untuk UI (Loading -> Data -> Error)
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    // State untuk Keranjang (Realtime update)
    val cartItems: StateFlow<List<CartItem>> = LocalCartRepository.cartItems

    // Init: Jalan otomatis saat ViewModel dibuat
    init {
        getProduk()
    }

    // --- 1. FITUR DATA PRODUK ---
    fun getProduk() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            homeUiState = try {
                val listProduk = repositori.getProduk()
                HomeUiState.Success(listProduk)
            } catch (e: IOException) {
                // Error Koneksi (WiFi mati / Server down)
                HomeUiState.Error
            } catch (e: HttpException) {
                // Error Server (404 / 500)
                HomeUiState.Error
            }
        }
    }

    // --- 2. FITUR AUTHENTICATION (LOGIN/LOGOUT) ---
    fun isUserLoggedIn(): Boolean {
        return repositori.getLoggedInUser() != null
    }

    fun logout() {
        repositori.logout()
        LocalCartRepository.clearCart() // Kosongkan keranjang saat logout
        // Opsional: Refresh produk atau navigasi ulang di UI
    }

    // --- 3. FITUR KERANJANG (CART) ---
    fun addToCart(produk: Produk) {
        if (isUserLoggedIn()) {
            LocalCartRepository.addItem(produk)
        }
    }

    fun removeFromCart(produkId: Int) {
        LocalCartRepository.removeItem(produkId)
    }

    fun getTotalCartPrice(): Double {
        return LocalCartRepository.getTotalPrice()
    }
}