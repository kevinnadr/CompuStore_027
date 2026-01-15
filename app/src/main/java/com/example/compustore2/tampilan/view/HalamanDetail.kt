package com.example.compustore2.tampilan.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.compustore2.R
import com.example.compustore2.tampilan.viewmodel.DetailUiState
import com.example.compustore2.tampilan.viewmodel.DetailViewModel
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel

// Hapus object DestinasiDetail dari sini jika sudah ada di folder 'route' agar tidak bentrok.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetail(
    itemId: Int, // <--- Pastikan parameter ini ADA
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit = {}, // Default value agar aman
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.detailUiState

    // Ambil data otomatis saat itemId berubah
    LaunchedEffect(itemId) {
        viewModel.getProdukById(itemId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Produk") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->

        when (uiState) {
            is DetailUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is DetailUiState.Success -> {
                val produk = uiState.produk
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = "http://10.0.2.2:3000/uploads/${produk.gambar ?: ""}",
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(250.dp),
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.ic_launcher_background)
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(produk.namaProduk ?: "Tanpa Nama", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("Rp ${String.format("%,.0f", produk.harga)}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Stok: ${produk.stok}")
                        Text("Deskripsi: ${produk.deskripsi ?: "-"}")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(modifier = Modifier.padding(16.dp)) {
                        Button(
                            onClick = { viewModel.addToCart(produk) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                            Text("Beli")
                        }
                    }
                }
            }
            is DetailUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.message, color = Color.Red)
                }
            }
        }
    }
}