package com.example.compustore2.service_api

import com.example.compustore2.model.Produk
import com.example.compustore2.model.RegisterRequest
import com.example.compustore2.model.RiwayatTransaksi
import com.example.compustore2.model.TransaksiRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CompustoreService {

    // --- PRODUK ---
    @GET("produk")
    suspend fun getProduk(): List<Produk>

    // --- RIWAYAT TRANSAKSI ---
    @GET("transaksi")
    suspend fun getRiwayatTransaksi(): List<RiwayatTransaksi>

    @GET("produk/{id}")
    suspend fun getProdukById(@Path("id") id: Int): Response<Produk>

    @POST("produk")
    suspend fun insertProduk(@Body produk: Produk): Response<Void>

    @PUT("produk/{id}") // <--- TAMBAHAN UNTUK UPDATE
    suspend fun updateProduk(@Path("id") id: Int, @Body produk: Produk): Response<Void>

    @DELETE("produk/{id}")
    suspend fun deleteProduk(@Path("id") id: Int): Response<Void>

    // --- TRANSAKSI ---
    @POST("transaksi")
    suspend fun createTransaksi(@Body transaksi: TransaksiRequest): Response<Void>

    // --- AUTH (REGISTER) ---
    @POST("register") // <--- TAMBAHAN UNTUK REGISTER
    suspend fun register(@Body request: RegisterRequest): Response<Void>
}