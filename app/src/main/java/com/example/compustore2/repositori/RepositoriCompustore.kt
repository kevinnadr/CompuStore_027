package com.example.compustore2.repositori

import com.example.compustore2.model.AuthResponse
import com.example.compustore2.model.LoginRequest
import com.example.compustore2.model.Produk
import com.example.compustore2.model.RegisterRequest
import com.example.compustore2.model.RiwayatTransaksi
import com.example.compustore2.model.TransaksiRequest
import com.example.compustore2.model.User
import com.example.compustore2.service_api.CompustoreService
import retrofit2.Response

interface RepositoriCompustore {
    suspend fun getProduk(): List<Produk>
    suspend fun login(loginRequest: LoginRequest): Response<AuthResponse>
    suspend fun register(registerRequest: RegisterRequest): Response<AuthResponse>

    suspend fun getRiwayatTransaksi(userId: String): List<RiwayatTransaksi>

    suspend fun insertProduk(produk: Produk): Response<Void>
    suspend fun updateProduk(id: Int, produk: Produk): Response<Void>
    suspend fun deleteProduk(id: Int): Response<Void>

    // Tambahkan baris ini
    suspend fun getProdukById(id: Int): Response<Produk>

    // ... di dalam interface RepositoriCompustore
    suspend fun createTransaksi(request: TransaksiRequest): Response<Void>

    // TAMBAHAN: Fungsi untuk Cek Login
    fun getLoggedInUser(): User?
    fun saveLoggedInUser(user: User)
    fun logout()
}

class NetworkRepositoriCompustore(
    private val compustoreService: CompustoreService
) : RepositoriCompustore {

    // Variable penyimpan sesi (Memory Session)
    private var loggedInUser: User? = null

    override suspend fun getProduk(): List<Produk> = compustoreService.getAllProduk()

    override suspend fun login(loginRequest: LoginRequest): Response<AuthResponse> {
        val response = compustoreService.loginUser(loginRequest)
        // Jika login sukses dari server, kita simpan otomatis di sini juga bisa
        if (response.isSuccessful && response.body()?.data != null) {
            loggedInUser = response.body()?.data
        }
        return response
    }

    override suspend fun register(registerRequest: RegisterRequest): Response<AuthResponse> {
        return compustoreService.registerUser(registerRequest)
    }

    // Implementasi Fungsi Session
    override fun getLoggedInUser(): User? = loggedInUser

    override fun saveLoggedInUser(user: User) {
        loggedInUser = user
    }

    override fun logout() {
        loggedInUser = null
    }

    // ... di dalam class NetworkRepositoriCompustore
    override suspend fun createTransaksi(request: TransaksiRequest): Response<Void> {
        return compustoreService.createTransaksi(request)
    }

    override suspend fun getRiwayatTransaksi(userId: String): List<RiwayatTransaksi> {
        return compustoreService.getRiwayatTransaksi(userId)
    }

    // Implementasi baris ini
    override suspend fun getProdukById(id: Int): Response<Produk> {
        return compustoreService.getProdukById(id)
    }

    // Implementasi
    override suspend fun insertProduk(produk: Produk) = compustoreService.insertProduk(produk)
    override suspend fun updateProduk(id: Int, produk: Produk) = compustoreService.updateProduk(id, produk)
    override suspend fun deleteProduk(id: Int) = compustoreService.deleteProduk(id)

}