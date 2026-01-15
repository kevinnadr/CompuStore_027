package com.example.compustore2.tampilan.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compustore2.model.RiwayatTransaksi
import com.example.compustore2.tampilan.CompustoreTopAppBar
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel
import com.example.compustore2.tampilan.viewmodel.RiwayatUiState
import com.example.compustore2.tampilan.viewmodel.RiwayatViewModel

@Composable
fun HalamanRiwayat(
    onNavigateBack: () -> Unit,
    viewModel: RiwayatViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val state = viewModel.uiState

    Scaffold(
        topBar = {
            CompustoreTopAppBar(
                title = "Riwayat Pesanan",
                canNavigateBack = false
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            when (state) {
                is RiwayatUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is RiwayatUiState.Success -> {
                    if (state.riwayat.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Belum ada riwayat transaksi.", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Tampilkan Data Real
                            items(state.riwayat) { transaksi ->
                                CardRiwayatItem(transaksi)
                            }
                        }
                    }
                }
                is RiwayatUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Gagal memuat riwayat", color = Color.Red)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.getRiwayat() }) {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Text(" Coba Lagi")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardRiwayatItem(transaksi: RiwayatTransaksi) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ID: #${transaksi.id}", // ID Real
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = transaksi.status, // Status Real
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Tanggal: ${transaksi.tanggal}", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total Belanja", fontSize = 12.sp, color = Color.Gray)
                // Total Harga Real
                Text(
                    text = "Rp ${String.format("%,.0f", transaksi.totalHarga)}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}