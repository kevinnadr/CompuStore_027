package com.example.compustore2.model


import com.google.gson.annotations.SerializedName


data class Produk(
    // PERBAIKAN 1: Di database Anda namanya 'produk_id', bukan 'id'
    @SerializedName("produk_id")
    val id: Int,

    // PERBAIKAN 2: Di database Anda namanya 'nama_produk'
    @SerializedName("nama_produk")
    val namaProduk: String?,

    @SerializedName("kategori")
    val kategori: String?,

    @SerializedName("merk")
    val merk: String?,

    @SerializedName("harga")
    val harga: Double,

    @SerializedName("stok")
    val stok: Int,

    @SerializedName("deskripsi")
    val deskripsi: String?,

    @SerializedName("gambar")
    val gambar: String?
)