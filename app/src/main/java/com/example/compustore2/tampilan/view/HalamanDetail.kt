package com.example.compustore2.tampilan.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.compustore2.tampilan.route.DestinasiNavigasi
import com.example.compustore2.tampilan.viewmodel.DetailUiState
import com.example.compustore2.tampilan.viewmodel.DetailViewModel
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel
import com.example.compustore2.R

object DestinasiDetail : DestinasiNavigasi {
    override val route = "detail_produk"
    override val titleRes = "Detail Produk"
    const val produkId = "itemId"
    val routeWithArgs = "$route/{$produkId}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetail(
    itemId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    // Ambil data saat halaman dibuka
    LaunchedEffect(itemId) {
        viewModel.getProdukById(itemId)
    }

    val uiState = viewModel.detailUiState
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Logic Admin (Sementara true)
    val isAdmin = true

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Produk") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) { Text("< Kembali") }
                }
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is DetailUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is DetailUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Gagal memuat data. Cek koneksi internet/server.")
                }
            }
            is DetailUiState.Success -> {
                val produk = uiState.produk

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Gambar Besar
                    AsyncImage(
                        model = "http://10.0.2.2:3000/uploads/${produk.gambar ?: ""}",
                        contentDescription = null,
                        error = painterResource(R.drawable.ic_launcher_background), // Pastikan icon ini ada
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- PERBAIKAN CRASH: Gunakan Elvis Operator (?:) ---
                    // Jika null, ganti dengan string default

                    Text(
                        text = produk.namaProduk ?: "Nama Tidak Tersedia",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Rp ${String.format("%,.0f", produk.harga)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    // Detail Lain (Safe Null)
                    DetailRow("Kategori", produk.kategori ?: "-")
                    DetailRow("Merk", produk.merk ?: "-")
                    DetailRow("Stok", "${produk.stok} unit")

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Deskripsi:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    // Safe Null untuk Deskripsi
                    Text(
                        text = produk.deskripsi ?: "Tidak ada deskripsi tersedia.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // TOMBOL AKSI ADMIN
                    if (isAdmin) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onNavigateToEdit(produk.id) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit")
                            }

                            Button(
                                onClick = { showDeleteDialog = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Hapus")
                            }
                        }
                    }
                }

                // Dialog Konfirmasi Hapus
                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Hapus Produk?") },
                        text = {
                            Text("Yakin ingin menghapus '${produk.namaProduk ?: "Item ini"}'?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.deleteProduk(produk.id) {
                                        showDeleteDialog = false
                                        onNavigateBack()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) { Text("Ya, Hapus") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        Text(": $value")
    }
}