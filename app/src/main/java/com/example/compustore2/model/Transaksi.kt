package com.example.compustore2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaksi(
    // Sesuaikan dengan nama kolom di Database MySQL Anda
    @SerialName("id") // Atau "transaksi_id" (Cek database Anda!)
    val id: Int,

    @SerialName("user_id")
    val userId: Int,

    @SerialName("total_harga") // Database mengirim 'total_harga'
    val total: Double,

    // Tanggal mungkin string dari MySQL
    // Jika di database tidak ada tanggal, hapus baris ini atau beri nilai default
    // @SerialName("tanggal")
    // val tanggal: String? = ""
)