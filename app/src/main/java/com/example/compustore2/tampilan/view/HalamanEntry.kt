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
import com.example.compustore2.tampilan.CompustoreTopAppBar

import com.example.compustore2.tampilan.viewmodel.EntryUiState
import com.example.compustore2.tampilan.viewmodel.EntryViewModel
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@Composable
fun HalamanEntry(
    onNavigateBack: () -> Unit,
    viewModel: EntryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CompustoreTopAppBar(
                title = "Tambah Produk",
                canNavigateBack = true,
                navigateUp = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // PERUBAHAN: Memanggil EntryBody (sebelumnya FormInput)
            EntryBody(
                entryUiState = viewModel.uiState,
                onValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.saveProduk()
                        onNavigateBack()
                    }
                }
            )
        }
    }
}

// PERUBAHAN PENTING:
// Nama fungsi diganti dari 'FormInput' menjadi 'EntryBody'
// agar bisa dipanggil (re-use) oleh HalamanUpdate.kt
@Composable
fun EntryBody(
    entryUiState: EntryUiState,
    onValueChange: (EntryUiState) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        OutlinedTextField(
            value = entryUiState.insertUiEvent.namaProduk,
            onValueChange = { onValueChange(entryUiState.copy(insertUiEvent = entryUiState.insertUiEvent.copy(namaProduk = it))) },
            label = { Text("Nama Produk") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = entryUiState.insertUiEvent.harga,
            onValueChange = { onValueChange(entryUiState.copy(insertUiEvent = entryUiState.insertUiEvent.copy(harga = it))) },
            label = { Text("Harga") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = entryUiState.insertUiEvent.stok,
            onValueChange = { onValueChange(entryUiState.copy(insertUiEvent = entryUiState.insertUiEvent.copy(stok = it))) },
            label = { Text("Stok") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = entryUiState.insertUiEvent.kategori,
            onValueChange = { onValueChange(entryUiState.copy(insertUiEvent = entryUiState.insertUiEvent.copy(kategori = it))) },
            label = { Text("Kategori") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = entryUiState.insertUiEvent.deskripsi,
            onValueChange = { onValueChange(entryUiState.copy(insertUiEvent = entryUiState.insertUiEvent.copy(deskripsi = it))) },
            label = { Text("Deskripsi") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}