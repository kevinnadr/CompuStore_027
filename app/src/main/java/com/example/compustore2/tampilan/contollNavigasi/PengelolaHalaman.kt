package com.example.compustore2.tampilan.contollNavigasi

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compustore2.tampilan.CompustoreBottomBar
import com.example.compustore2.tampilan.route.DestinasiDetail
import com.example.compustore2.tampilan.route.DestinasiHome
import com.example.compustore2.tampilan.route.DestinasiLogin
import com.example.compustore2.tampilan.route.DestinasiRegister
import com.example.compustore2.tampilan.view.DestinasiCheckout
import com.example.compustore2.tampilan.view.DestinasiEntry
import com.example.compustore2.tampilan.view.DestinasiProfile
import com.example.compustore2.tampilan.view.DestinasiRiwayat
import com.example.compustore2.tampilan.view.HalamanCheckout
import com.example.compustore2.tampilan.view.HalamanDetail
import com.example.compustore2.tampilan.view.HalamanEntry
import com.example.compustore2.tampilan.view.HalamanLogin
import com.example.compustore2.tampilan.view.HalamanProfile
import com.example.compustore2.tampilan.view.HalamanRegister
import com.example.compustore2.tampilan.view.HalamanRiwayat
import com.example.compustore2.ui.view.HalamanHome


@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController()
) {
    // 1. Cek Rute Saat Ini (Untuk menyembunyikan BottomBar di halaman Login/Checkout/Entry)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Daftar halaman yang WAJIB pakai Bottom Bar
    val showBottomBar = currentRoute in listOf(
        DestinasiHome.route,
        DestinasiRiwayat.route,
        DestinasiProfile.route
    )

    Scaffold(
        // 2. Pasang Bottom Bar (Hanya muncul di Home, Riwayat, Profile)
        bottomBar = {
            if (showBottomBar) {
                CompustoreBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->

        // 3. Konten Navigasi Utama
        NavHost(
            navController = navController,
            startDestination = DestinasiHome.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // --- MENU UTAMA (Ada Bottom Bar) ---

            // Halaman Home
            composable(DestinasiHome.route) {
                HalamanHome(
                    onNavigateToLogin = { navController.navigate(DestinasiLogin.route) },
                    onNavigateToCheckout = { navController.navigate(DestinasiCheckout.route) },
                    onNavigateToEntry = { navController.navigate(DestinasiEntry.route) },
                    onNavigateToDetail = { produkId ->
                        // Pindah ke Detail membawa ID Barang
                        navController.navigate("${DestinasiDetail.route}/$produkId")
                    }
                )
            }

            // Halaman Riwayat
            composable(DestinasiRiwayat.route) {
                HalamanRiwayat(
                    onBack = { navController.navigate(DestinasiHome.route) }
                )
            }

            // Halaman Profile
            composable(DestinasiProfile.route) {
                HalamanProfile(
                    onLogout = {
                        // Logout: Reset ke Login & Hapus Backstack
                        navController.navigate(DestinasiLogin.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // --- HALAMAN AUTH & TRANSAKSI & ADMIN (Tanpa Bottom Bar) ---

            // Halaman Login
            composable(DestinasiLogin.route) {
                HalamanLogin(
                    onLoginSuccess = { navController.popBackStack() },
                    onNavigateToRegister = { navController.navigate(DestinasiRegister.route) }
                )
            }

            // Halaman Register
            composable(DestinasiRegister.route) {
                HalamanRegister(
                    onRegisterSuccess = { navController.popBackStack() }
                )
            }

            // Halaman Checkout
            composable(DestinasiCheckout.route) {
                HalamanCheckout(
                    onCheckoutSuccess = {
                        // Sukses Checkout -> Pindah ke Riwayat
                        navController.navigate(DestinasiRiwayat.route) {
                            popUpTo(DestinasiHome.route)
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // Halaman Entry (Tambah Barang - Admin)
            composable(DestinasiEntry.route) {
                HalamanEntry(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Halaman Detail Produk
            composable(
                route = DestinasiDetail.routeWithArgs,
                arguments = listOf(navArgument(DestinasiDetail.produkId) {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt(DestinasiDetail.produkId)
                itemId?.let {
                    HalamanDetail(
                        itemId = it,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToEdit = { id ->
                            // Nanti diarahkan ke halaman Edit (Next step)
                            // navController.navigate("${DestinasiEdit.route}/$id")
                        }
                    )
                }
            }
        }
    }
}