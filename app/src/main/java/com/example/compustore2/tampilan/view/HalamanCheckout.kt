package com.example.compustore2.tampilan.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compustore2.tampilan.viewmodel.CheckoutUiState
import com.example.compustore2.tampilan.viewmodel.CheckoutViewModel
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanCheckout(
    onBack: () -> Unit,
    onCheckoutSuccess: () -> Unit,
    viewModel: CheckoutViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    // Ambil data dari ViewModel
    val cartItems by viewModel.cartItems.collectAsState()
    val checkoutState by viewModel.checkoutState.collectAsState()

    // Hitung Total
    val totalHarga = cartItems.sumOf { it.produk.harga * it.jumlah }

    // Pantau status Checkout (Loading / Success / Error)
    LaunchedEffect(checkoutState) {
        if (checkoutState is CheckoutUiState.Success) {
            onCheckoutSuccess() // Pindah ke Home jika sukses
            viewModel.resetState() // Reset status agar tidak looping
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Daftar Barang yang akan dibeli
            Text("Ringkasan Pesanan", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item.produk.namaProduk} x${item.jumlah}")
                        Text("Rp ${String.format("%,.0f", item.produk.harga * item.jumlah)}")
                    }
                    Divider()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total Harga
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Bayar:", fontWeight = FontWeight.Bold)
                    Text("Rp ${String.format("%,.0f", totalHarga)}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tampilkan Error jika ada
            if (checkoutState is CheckoutUiState.Error) {
                Text(
                    text = (checkoutState as CheckoutUiState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Tombol Bayar
            Button(
                onClick = { viewModel.prosesCheckout(totalHarga) },
                modifier = Modifier.fillMaxWidth(),
                enabled = checkoutState !is CheckoutUiState.Loading && cartItems.isNotEmpty()
            ) {
                if (checkoutState is CheckoutUiState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Bayar Sekarang")
                }
            }
        }
    }
}