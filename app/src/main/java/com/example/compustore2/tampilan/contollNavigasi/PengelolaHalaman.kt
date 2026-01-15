package com.example.compustore2.tampilan.contollNavigasi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compustore2.tampilan.route.DestinasiDetail
import com.example.compustore2.tampilan.route.DestinasiEntry
import com.example.compustore2.tampilan.route.DestinasiHome
import com.example.compustore2.tampilan.route.DestinasiRiwayat
import com.example.compustore2.tampilan.route.DestinasiUpdate
import com.example.compustore2.tampilan.view.*

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = modifier
    ) {
        // 1. HOME
        composable(DestinasiHome.route) {
            HalamanHome(
                // Sambungkan tombol (+) ke halaman Entry
                onNavigateToEntry = {
                    navController.navigate(DestinasiEntry.route)
                },
                // Sambungkan klik barang ke halaman Detail
                onNavigateToDetail = { itemId ->
                    navController.navigate("${DestinasiDetail.route}/$itemId")
                },
                // Sambungkan tombol keranjang/checkout
                onNavigateToCheckout = {
                    navController.navigate("checkout")
                }
            )
        }

        // 2. ENTRY (TAMBAH BARANG)
        composable(DestinasiEntry.route) {
            HalamanEntry(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 3. DETAIL BARANG
        composable(
            route = DestinasiDetail.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetail.produkId) { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt(DestinasiDetail.produkId)
            itemId?.let {
                HalamanDetail(
                    itemId = it,
                    onNavigateBack = { navController.popBackStack() },
                    // Sambungkan tombol Edit ke Halaman Update
                    onNavigateToEdit = {
                        navController.navigate("${DestinasiUpdate.route}/$it")
                    }
                )
            }
        }

        // 4. UPDATE (EDIT BARANG)
        composable(
            route = DestinasiUpdate.routeWithArgs,
            arguments = listOf(navArgument(DestinasiUpdate.itemId) { type = NavType.IntType })
        ) {
            HalamanUpdate(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 5. CHECKOUT
        composable("checkout") {
            HalamanCheckout(
                onBack = { navController.popBackStack() },
                onCheckoutSuccess = {
                    navController.navigate(DestinasiHome.route) {
                        popUpTo(DestinasiHome.route) { inclusive = true }
                    }
                }
            )
        }

        // 6. PROFILE
        composable("profile") {
            HalamanProfile(
                onBack = { navController.navigate(DestinasiHome.route) },
                onLogout = { /* Logika Logout nanti */ },
                onLogin = { navController.navigate("login") }
            )
        }

        // 7. LOGIN & REGISTER
        composable("login") {
            HalamanLogin(
                onLoginSuccess = { navController.navigate("profile") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            HalamanRegister(
                onRegisterSuccess = { navController.navigate("login") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(DestinasiRiwayat.route) {
            HalamanRiwayat(
                onNavigateBack = { navController.navigate(DestinasiHome.route) }
            )
        }
    }
}