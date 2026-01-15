package com.example.compustore2.tampilan

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.compustore2.tampilan.route.DestinasiHome
import com.example.compustore2.tampilan.route.DestinasiRiwayat

@Composable
fun CompustoreBottomAppBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White, // Background Putih Bersih
        tonalElevation = 8.dp, // Bayangan halus
        modifier = modifier
    ) {
        // 1. HOME
        AddItem(
            screenRoute = DestinasiHome.route,
            currentRoute = currentRoute,
            icon = Icons.Default.Home,
            label = "Home",
            navController = navController
        )

        // 2. RIWAYAT (BARU)
        AddItem(
            screenRoute = DestinasiRiwayat.route,
            currentRoute = currentRoute,
            icon = Icons.Default.List, // Ikon List/Riwayat
            label = "Riwayat",
            navController = navController
        )

        // 3. PROFILE
        AddItem(
            screenRoute = "profile",
            currentRoute = currentRoute,
            icon = Icons.Default.Person,
            label = "Profile",
            navController = navController
        )
    }
}

@Composable
fun androidx.compose.foundation.layout.RowScope.AddItem(
    screenRoute: String,
    currentRoute: String?,
    icon: ImageVector,
    label: String,
    navController: NavHostController
) {
    val selected = currentRoute == screenRoute
    NavigationBarItem(
        label = { Text(label) },
        icon = { Icon(icon, contentDescription = label) },
        selected = selected,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            unselectedIconColor = Color.Gray,
            unselectedTextColor = Color.Gray
        ),
        onClick = {
            navController.navigate(screenRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}