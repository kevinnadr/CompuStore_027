package com.example.compustore2.model


data class CartItem(
    val produk: Produk,
    var jumlah: Int = 1
) {
    val totalHarga: Double
        get() = produk.harga * jumlah
}