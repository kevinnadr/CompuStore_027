package com.example.compustore2.tampilan.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compustore2.R // Pastikan R di-import
import com.example.compustore2.tampilan.CompustoreBottomAppBar
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel
import com.example.compustore2.tampilan.viewmodel.ProfileViewModel

@Composable
fun HalamanProfile(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {},
    onLogin: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    // 1. Ambil Data Terbaru dari ViewModel (Ganti cara lama)
    val uiState by viewModel.uiState.collectAsState()

    // Cek apakah user sudah login atau belum
    if (uiState.isLogin) {
        // TAMPILAN JIKA SUDAH LOGIN
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Foto Profil
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .padding(16.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nama & Email (Ambil dari uiState)
            Text(
                text = uiState.username, // Data Real
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = uiState.email,    // Data Real
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Info Tambahan (Card)
            CardInfo(label = "Role", value = "Member") // Default Member
            CardInfo(label = "Status", value = "Aktif")

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Logout
            Button(
                onClick = {
                    viewModel.logout()
                    onLogout() // Navigasi ke halaman login/home setelah logout
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Logout")
            }
        }
    } else {
        // TAMPILAN JIKA BELUM LOGIN (Guest)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Anda belum login")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onLogin) {
                    Text("Login Sekarang")
                }
            }
        }
    }
}

@Composable
fun CardInfo(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color.Gray)
            Text(value, fontWeight = FontWeight.SemiBold)
        }
    }
}