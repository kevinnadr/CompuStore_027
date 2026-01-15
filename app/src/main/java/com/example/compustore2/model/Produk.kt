package com.example.compustore2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Produk(
    // PERBAIKAN UTAMA: Sesuaikan dengan nama kolom di Database Anda
    @SerialName("produk_id")
    val id: Int = 0,

    @SerialName("nama_produk")
    val namaProduk: String? = "",

    val kategori: String? = "",
    val merk: String? = "",

    val harga: Double = 0.0,

    val stok: Int = 0,

    val deskripsi: String? = "",
    val gambar: String? = ""
)