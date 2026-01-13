package com.example.compustore2.model


import com.google.gson.annotations.SerializedName

// Struktur utama pesanan (Header)
data class TransaksiRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("total_harga") val totalHarga: Double,
    @SerializedName("metode_pembayaran") val metodePembayaran: String,
    @SerializedName("metode_pengiriman") val metodePengiriman: String,
    @SerializedName("detail_barang") val detailBarang: List<DetailTransaksiRequest>
)

// Struktur detail barang (Isi Pesanan)
data class DetailTransaksiRequest(
    @SerializedName("produk_id") val produkId: Int,
    @SerializedName("jumlah") val jumlah: Int,
    @SerializedName("harga_satuan") val hargaSatuan: Double,
    @SerializedName("subtotal") val subtotal: Double
)