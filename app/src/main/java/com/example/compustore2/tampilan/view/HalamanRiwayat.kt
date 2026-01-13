package com.example.compustore2.tampilan.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compustore2.model.RiwayatTransaksi
import com.example.compustore2.tampilan.route.DestinasiNavigasi
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel
import com.example.compustore2.tampilan.viewmodel.RiwayatUiState
import com.example.compustore2.tampilan.viewmodel.RiwayatViewModel


object DestinasiRiwayat : DestinasiNavigasi {
    override val route = "riwayat"
    override val titleRes = "Riwayat Pesanan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanRiwayat(
    onBack: () -> Unit,
    viewModel: RiwayatViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Riwayat Pesanan") },
                navigationIcon = {
                    // Tombol Back (Opsional, jika ingin kembali ke Home)
                    TextButton(onClick = onBack) { Text("< Home") }
                }
            )
        }
    ) { innerPadding ->
        val state = viewModel.uiState

        when (state) {
            is RiwayatUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is RiwayatUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Gagal memuat riwayat / Belum login") }
            is RiwayatUiState.Success -> {
                if (state.riwayat.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Belum ada transaksi") }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(innerPadding).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.riwayat) { transaksi ->
                            CardRiwayat(transaksi)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardRiwayat(transaksi: RiwayatTransaksi) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "ID: #${transaksi.transaksiId}", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = java.lang.String.valueOf(transaksi.tanggal).take(10), // Ambil tanggal saja (YYYY-MM-DD) — defensive conversion
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: Rp ${String.format("%,.0f", transaksi.totalHarga)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Bayar: ${java.lang.String.valueOf(transaksi.metodePembayaran)}", style = MaterialTheme.typography.bodyMedium)

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusChip(label = java.lang.String.valueOf(transaksi.statusPembayaran), color = if(transaksi.statusPembayaran == "Lunas") Color.Green else Color.Yellow)
                StatusChip(label = java.lang.String.valueOf(transaksi.statusPengiriman), color = Color.Cyan)
            }
        }
    }
}

@Composable
fun StatusChip(label: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = java.lang.String.valueOf(label),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black
        )
    }
}