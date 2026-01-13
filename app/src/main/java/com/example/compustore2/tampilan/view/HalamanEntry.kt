package com.example.compustore2.tampilan.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compustore2.tampilan.route.DestinasiNavigasi
import com.example.compustore2.tampilan.viewmodel.EntryViewModel
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

object DestinasiEntry : DestinasiNavigasi {
    override val route = "entry_produk"
    override val titleRes = "Tambah Barang"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEntry(
    onNavigateBack: () -> Unit,
    viewModel: EntryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Tambah Barang Baru") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            val uiState = viewModel.uiState

            OutlinedTextField(
                value = uiState.nama,
                onValueChange = { viewModel.updateUiState(uiState.copy(nama = it)) },
                label = { Text("Nama Produk") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.kategori,
                onValueChange = { viewModel.updateUiState(uiState.copy(kategori = it)) },
                label = { Text("Kategori (Laptop/Mouse/dll)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.merk,
                onValueChange = { viewModel.updateUiState(uiState.copy(merk = it)) },
                label = { Text("Merk") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.harga,
                onValueChange = { viewModel.updateUiState(uiState.copy(harga = it)) },
                label = { Text("Harga") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.stok,
                onValueChange = { viewModel.updateUiState(uiState.copy(stok = it)) },
                label = { Text("Stok") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.deskripsi,
                onValueChange = { viewModel.updateUiState(uiState.copy(deskripsi = it)) },
                label = { Text("Deskripsi") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveProduk()
                        onNavigateBack() // Kembali setelah simpan
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Barang")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Batal")
            }
        }
    }
}