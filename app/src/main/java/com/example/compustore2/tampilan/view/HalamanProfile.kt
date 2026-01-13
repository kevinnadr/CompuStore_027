package com.example.compustore2.tampilan.view


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compustore2.tampilan.route.DestinasiNavigasi
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel
import com.example.compustore2.tampilan.viewmodel.ProfileViewModel


object DestinasiProfile : DestinasiNavigasi {
    override val route = "profile"
    override val titleRes = "Profile"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanProfile(
    onLogout: () -> Unit,
    onBack: () -> Unit, // Opsional jika belum ada bottom bar
    viewModel: ProfileViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val user = viewModel.currentUser

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Profile Saya") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ikon Profile Besar
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (user != null) {
                // Info User
                ProfileItem(label = "Nama", value = user.nama)
                ProfileItem(label = "Email", value = user.email)
                ProfileItem(label = "Role", value = user.role)
                // ProfileItem(label = "No HP", value = user.noHp) // Jika ada di model User
            } else {
                Text("User tidak ditemukan (Anda belum login)")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Logout
            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keluar Aplikasi")
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Text(text = java.lang.String.valueOf(value), style = MaterialTheme.typography.bodyLarge)
        Divider()
    }
}