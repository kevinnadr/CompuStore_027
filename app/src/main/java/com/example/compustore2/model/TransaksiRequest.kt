package com.example.compustore2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransaksiRequest(
    @SerialName("user_id")
    val userId: Int,

    @SerialName("total_harga")
    val total: Double,

    val status: String,

    val tanggal: String,
    val alamat: String,

    // --- TAMBAHAN BARU: METODE PEMBAYARAN ---
    @SerialName("metode_pembayaran") // Sesuaikan dengan nama kolom di database backend
    val metodePembayaran: String,
    // ----------------------------------------

    @SerialName("detail")
    val items: List<DetailTransaksiRequest>
)

@Serializable
data class DetailTransaksiRequest(
    @SerialName("produk_id")
    val produkId: Int,
    val jumlah: Int,
    @SerialName("subtotal")
    val harga: Double
)