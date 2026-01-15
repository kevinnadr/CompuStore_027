package com.example.compustore2.repositori

import android.content.Context
import com.example.compustore2.model.CartItem
import com.example.compustore2.model.Produk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocalCartRepositori(context: Context) {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(produk: Produk) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.produk.id == produk.id }
            if (existingItem != null) {
                currentItems.map { if (it.produk.id == produk.id) it.copy(jumlah = it.jumlah + 1) else it }
            } else {
                currentItems + CartItem(produk, 1)
            }
        }
    }

    fun removeFromCart(produkId: Int) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.produk.id == produkId }
            if (existingItem != null && existingItem.jumlah > 1) {
                currentItems.map { if (it.produk.id == produkId) it.copy(jumlah = it.jumlah - 1) else it }
            } else {
                currentItems.filter { it.produk.id != produkId }
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}