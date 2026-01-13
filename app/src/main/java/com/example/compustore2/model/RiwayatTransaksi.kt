package com.example.compustore2.model


import com.google.gson.annotations.SerializedName

data class RiwayatTransaksi(
    @SerializedName("transaksi_id") val transaksiId: Int,
    @SerializedName("tanggal_transaksi") val tanggal: String,
    @SerializedName("total_harga") val totalHarga: Double,
    @SerializedName("status_pembayaran") val statusPembayaran: String,
    @SerializedName("status_pengiriman") val statusPengiriman: String,
    @SerializedName("metode_pembayaran") val metodePembayaran: String
)