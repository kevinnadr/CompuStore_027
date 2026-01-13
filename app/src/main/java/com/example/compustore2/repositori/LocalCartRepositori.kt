package com.example.compustore2.repositori


import com.example.compustore2.model.CartItem
import com.example.compustore2.model.Produk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object LocalCartRepository {
    // StateFlow agar UI otomatis update jika data berubah
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Tambah barang ke keranjang
    fun addItem(produk: Produk) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.produk.id == produk.id }
            if (existingItem != null) {
                // Jika barang sudah ada, tambah jumlahnya
                currentList.map {
                    if (it.produk.id == produk.id) it.copy(jumlah = it.jumlah + 1) else it
                }
            } else {
                // Jika belum ada, tambahkan baru
                currentList + CartItem(produk, 1)
            }
        }
    }

    // Kurangi jumlah item
    fun removeItem(produkId: Int) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.produk.id == produkId }
            if (existingItem != null) {
                if (existingItem.jumlah > 1) {
                    currentList.map {
                        if (it.produk.id == produkId) it.copy(jumlah = it.jumlah - 1) else it
                    }
                } else {
                    currentList.filter { it.produk.id != produkId }
                }
            } else {
                currentList
            }
        }
    }

    // Hapus semua (setelah checkout)
    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Hitung Total Harga
    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.totalHarga }
    }
}