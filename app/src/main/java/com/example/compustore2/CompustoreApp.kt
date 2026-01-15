package com.example.compustore2

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compustore2.tampilan.CompustoreBottomAppBar // <--- Import file custom kita
import com.example.compustore2.tampilan.contollNavigasi.PengelolaHalaman
import com.example.compustore2.tampilan.route.DestinasiHome
import com.example.compustore2.tampilan.route.DestinasiRiwayat

@Composable
fun CompustoreApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Tentukan kapan bottom bar muncul
    val showBottomBar = currentRoute == DestinasiHome.route ||
            currentRoute == "profile" ||
            currentRoute == DestinasiRiwayat.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                // PANGGIL DI SINI:
                CompustoreBottomAppBar(navController = navController)
            }
        }
    ) { innerPadding ->
        PengelolaHalaman(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}