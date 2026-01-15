package com.example.compustore2.tampilan.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compustore2.tampilan.CompustoreTopAppBar
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel
import com.example.compustore2.tampilan.viewmodel.UpdateViewModel
import kotlinx.coroutines.launch

@Composable
fun HalamanUpdate(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CompustoreTopAppBar(
                title = "Edit Produk",
                canNavigateBack = true,
                navigateUp = onNavigateBack
            )
        }
    ) { innerPadding ->

        // Panggil Form yang sama dengan Halaman Entry
        EntryBody(
            modifier = Modifier.padding(innerPadding),

            // PARAMETER YANG BENAR:
            entryUiState = viewModel.uiState,
            onValueChange = viewModel::updateUiState,

            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateProduk()
                    onNavigateBack() // Kembali setelah sukses update
                }
            }
        )
    }
}