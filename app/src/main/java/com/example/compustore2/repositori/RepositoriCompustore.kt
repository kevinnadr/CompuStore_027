package com.example.compustore2.repositori

import com.example.compustore2.model.CartItem
import com.example.compustore2.model.Produk
import com.example.compustore2.model.RegisterRequest
import com.example.compustore2.model.RiwayatTransaksi
import com.example.compustore2.model.TransaksiRequest
import com.example.compustore2.service_api.CompustoreService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Response

class RepositoriCompustore(private val compustoreService: CompustoreService) {

    // --- CRUD PRODUK ---

    suspend fun getProduk(): List<Produk> = compustoreService.getProduk()

    suspend fun getProdukById(id: Int): Response<Produk> = compustoreService.getProdukById(id)

    suspend fun insertProduk(produk: Produk): Response<Void> = compustoreService.insertProduk(produk)

    // TAMBAHAN: UPDATE
    suspend fun updateProduk(id: Int, produk: Produk): Response<Void> {
        return compustoreService.updateProduk(id, produk)
    }

    suspend fun deleteProduk(id: Int): Response<Void> = compustoreService.deleteProduk(id)


    // --- TRANSAKSI & RIWAYAT ---

    suspend fun createTransaksi(transaksi: TransaksiRequest): Response<Void> {
        return compustoreService.createTransaksi(transaksi)
    }

    // UBAH BAGIAN INI:
    suspend fun getRiwayatTransaksi(): List<RiwayatTransaksi> {
        return compustoreService.getRiwayatTransaksi()
    }

    // --- AUTH (REGISTER) ---
    // TAMBAHAN: REGISTER
    suspend fun register(request: RegisterRequest): Response<Void> {
        return compustoreService.register(request)
    }


    // --- KERANJANG LOCAL (StateFlow) ---

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(produk: Produk) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.produk.id == produk.id }
            if (existingItem != null) {
                currentItems.map {
                    if (it.produk.id == produk.id) it.copy(jumlah = it.jumlah + 1) else it
                }
            } else {
                currentItems + CartItem(produk, 1)
            }
        }
    }

    fun removeFromCart(produkId: Int) {
        _cartItems.update { currentItems ->
            currentItems.filter { it.produk.id != produkId }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}