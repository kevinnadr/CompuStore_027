package com.example.compustore2.tampilan


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.compustore2.tampilan.route.DestinasiHome
import com.example.compustore2.tampilan.view.DestinasiProfile
import com.example.compustore2.tampilan.view.DestinasiRiwayat

// Data Class untuk Item Menu
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun CompustoreBottomBar(navController: NavController) {
    // Daftar Menu
    val items = listOf(
        BottomNavItem(DestinasiHome.route, Icons.Default.Home, "Home"),
        BottomNavItem(DestinasiRiwayat.route, Icons.Default.DateRange, "Riwayat"),
        BottomNavItem(DestinasiProfile.route, Icons.Default.Person, "Profile")
    )

    // Cek halaman mana yang sedang aktif
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        items.forEach { item ->
            // Menentukan apakah item ini sedang dipilih
            val selected = currentRoute == item.route

            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selected,
                onClick = {
                    // Logika Pindah Halaman
                    navController.navigate(item.route) {
                        // Agar tidak menumpuk halaman Home berkali-kali saat ditekan
                        popUpTo(DestinasiHome.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}