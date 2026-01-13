package com.example.compustore2.service_api
import com.example.compustore2.model.AuthResponse
import com.example.compustore2.model.LoginRequest
import com.example.compustore2.model.Produk
import com.example.compustore2.model.RegisterRequest
import com.example.compustore2.model.RiwayatTransaksi
import com.example.compustore2.model.TransaksiRequest
import com.example.compustore2.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CompustoreService {

    // --- USER / AUTH ---

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<AuthResponse>


    // --- PRODUK ---

    @GET("produk")
    suspend fun getAllProduk(): List<Produk>

    @GET("produk/{id}")
    suspend fun getProdukById(@Path("id") id: Int): Response<Produk>

    @POST("produk")
    suspend fun insertProduk(@Body produk: Produk): Response<Void>

    @PUT("produk/{id}")
    suspend fun updateProduk(@Path("id") id: Int, @Body produk: Produk): Response<Void>

    @DELETE("produk/{id}")
    suspend fun deleteProduk(@Path("id") id: Int): Response<Void>


    // --- TRANSAKSI ---

    @GET("transaksi/user/{userId}")
    suspend fun getRiwayatTransaksi(@Path("userId") userId: String): List<RiwayatTransaksi>

    @POST("transaksi")
    suspend fun createTransaksi(@Body request: TransaksiRequest): Response<Void>
