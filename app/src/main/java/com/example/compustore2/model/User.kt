package com.example.compustore2.model

import com.google.gson.annotations.SerializedName

// Model untuk data User (Respon dari server saat berhasil login)
data class User(
    @SerializedName("user_id") val userId: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String, // "admin" atau "user"
    @SerializedName("token") val token: String? = null // Jika nanti pakai token
)

// Model untuk respon Login/Register (Format JSON dari backend)
data class AuthResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: User? // Bisa null jika login gagal
)

// Model untuk mengirim data saat Login
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

// Model untuk mengirim data saat Register
data class RegisterRequest(
    @SerializedName("nama") val nama: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("no_hp") val noHp: String,
    @SerializedName("role") val role: String = "user" // Default user
)