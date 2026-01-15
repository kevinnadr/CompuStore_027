package com.example.compustore2.tampilan.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.compustore2.CompustoreApplication

object PenyediaViewModel {
    val Factory = viewModelFactory {

        // 1. HomeViewModel
        initializer {
            HomeViewModel(
                repositori = compustoreApp().container.repositoriCompustore
            )
        }

        // 2. DetailViewModel
        initializer {
            DetailViewModel(
                repositori = compustoreApp().container.repositoriCompustore
            )
        }

        // 3. EntryViewModel
        initializer {
            EntryViewModel(
                repositori = compustoreApp().container.repositoriCompustore
            )
        }

        // 4. UpdateViewModel
        initializer {
            UpdateViewModel(
                savedStateHandle = createSavedStateHandle(),
                repositori = compustoreApp().container.repositoriCompustore
            )
        }

        // 5. CheckoutViewModel
        initializer {
            CheckoutViewModel(
                repositori = compustoreApp().container.repositoriCompustore
            )
        }

        // 6. LoginViewModel (INI YANG BIKIN ERROR SEBELUMNYA)
        initializer {
            LoginViewModel(
                repositori = compustoreApp().container.repositoriCompustore,
                // Tambahkan baris ini agar error hilang:
                userPreferencesRepository = compustoreApp().container.userPreferencesRepository
            )
        }

        // 7. RegisterViewModel
        initializer {
            RegisterViewModel(
                repositori = compustoreApp().container.repositoriCompustore
            )
        }

        // 8. RiwayatViewModel
        initializer {
            RiwayatViewModel(
                repositori = compustoreApp().container.repositoriCompustore
            )
        }

        // 9. ProfileViewModel (Biasanya juga butuh UserPreferences)
        initializer {
            ProfileViewModel(
                repositori = compustoreApp().container.repositoriCompustore,
                // Tambahkan ini jika ProfileViewModel Anda memintanya juga
                userPreferencesRepository = compustoreApp().container.userPreferencesRepository
            )
        }
    }
}

// Extension function
fun CreationExtras.compustoreApp(): CompustoreApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CompustoreApplication)