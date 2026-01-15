package com.example.compustore2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RiwayatTransaksi(
    val id: Int = 0, // Default 0 jika id tidak ditemukan

    // Cek nama kolom di database Anda, apakah 'total_harga', 'total', atau 'harga'?
    // Gunakan @SerialName("nama_di_database")
    @SerialName("total_harga")
    val totalHarga: Double = 0.0, // Default 0.0 agar tidak crash

    val tanggal: String = "-", // Default "-"

    val status: String = "Selesai" // Default "Selesai"
)