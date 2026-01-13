package com.example.compustore2.tampilan.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compustore2.tampilan.route.DestinasiNavigasi
import com.example.compustore2.tampilan.viewmodel.CheckoutUiState
import com.example.compustore2.tampilan.viewmodel.CheckoutViewModel
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel


object DestinasiCheckout : DestinasiNavigasi {
    override val route = "checkout"
    override val titleRes = "Checkout"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanCheckout(
    onCheckoutSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: CheckoutViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val context = LocalContext.current
    val checkoutState = viewModel.uiState

    // Cek Status Transaksi
    LaunchedEffect(checkoutState) {
        when (checkoutState) {
            is CheckoutUiState.Success -> {
                Toast.makeText(context, "Pesanan Berhasil Dibuat!", Toast.LENGTH_LONG).show()
                onCheckoutSuccess()
            }
            is CheckoutUiState.Error -> {
                Toast.makeText(context, checkoutState.message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Checkout") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // 1. Ringkasan Pesanan
            Text("Ringkasan Pesanan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                items(viewModel.cartItems) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item.produk.namaProduk} (x${item.jumlah})", modifier = Modifier.weight(1f))
                        Text("Rp ${String.format("%,.0f", item.totalHarga)}")
                    }
                    Divider()
                }
            }

            // 2. Total Harga
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total Bayar:", style = MaterialTheme.typography.titleLarge)
                Text("Rp ${String.format("%,.0f", viewModel.totalPrice)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 3. Pilihan Metode Pembayaran
            Text("Metode Pembayaran", style = MaterialTheme.typography.titleMedium)
            Row {
                RadioButton(selected = viewModel.metodePembayaran == "Transfer", onClick = { viewModel.metodePembayaran = "Transfer" })
                Text("Transfer", modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(selected = viewModel.metodePembayaran == "COD", onClick = { viewModel.metodePembayaran = "COD" })
                Text("COD", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4. Pilihan Pengiriman
            Text("Metode Pengiriman", style = MaterialTheme.typography.titleMedium)
            Row {
                RadioButton(selected = viewModel.metodePengiriman == "Diantar", onClick = { viewModel.metodePengiriman = "Diantar" })
                Text("Diantar", modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(selected = viewModel.metodePengiriman == "Pickup", onClick = { viewModel.metodePengiriman = "Pickup" })
                Text("Pickup", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Tombol Aksi
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Batal") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.processCheckout() },
                    modifier = Modifier.weight(1f),
                    enabled = checkoutState != CheckoutUiState.Loading
                ) {
                    if (checkoutState == CheckoutUiState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Bayar Sekarang")
                    }
                }
            }
        }
    }
}