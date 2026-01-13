package com.example.compustore2.tampilan.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.compustore2.model.User
import com.example.compustore2.repositori.RepositoriCompustore


class ProfileViewModel(private val repositori: RepositoriCompustore) : ViewModel() {

    var currentUser: User? by mutableStateOf(null)
        private set

    init {
        getCurrentUser()
    }

    fun getCurrentUser() {
        currentUser = repositori.getLoggedInUser()
    }

    fun logout() {
        repositori.logout()
        // Reset user
        currentUser = null
    }
}