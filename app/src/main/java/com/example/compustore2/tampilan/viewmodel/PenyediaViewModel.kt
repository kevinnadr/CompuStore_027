package com.example.compustore2.tampilan.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.compustore2.CompustoreApplication


object PenyediaViewModel {
    val Factory = viewModelFactory {

        // 1. Initializer untuk HomeViewModel
        initializer {
            HomeViewModel(aplikasiCompustore().container.repositoriCompustore)
        }

        // 2. Initializer untuk LoginViewModel
        initializer {
            LoginViewModel(aplikasiCompustore().container.repositoriCompustore)
        }

        // 3. Initializer untuk RegisterViewModel
        initializer {
            RegisterViewModel(aplikasiCompustore().container.repositoriCompustore)
        }

        // 4. Initializer untuk CheckoutViewModel (INI YANG KURANG)
        initializer {
            CheckoutViewModel(aplikasiCompustore().container.repositoriCompustore)
        }

        // 5. Initializer untuk RiwayatViewModel (Jika sudah dibuat)
        initializer {
            RiwayatViewModel(aplikasiCompustore().container.repositoriCompustore)
        }
        initializer {
            ProfileViewModel(aplikasiCompustore().container.repositoriCompustore)
        }

        initializer {
            EntryViewModel(aplikasiCompustore().container.repositoriCompustore)
        }

        initializer {
            DetailViewModel(aplikasiCompustore().container.repositoriCompustore)
        }

    }
}

// Fungsi ekstensi untuk mempermudah akses
fun CreationExtras.aplikasiCompustore(): CompustoreApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CompustoreApplication)