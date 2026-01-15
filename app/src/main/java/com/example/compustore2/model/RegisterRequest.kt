package com.example.compustore2.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val nama: String,
    val email: String,
    val password: String
)