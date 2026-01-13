package com.example.compustore2.tampilan.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compustore2.R
import com.example.compustore2.tampilan.route.DestinasiNavigasi
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel
import com.example.compustore2.tampilan.viewmodel.RegisterUiState
import com.example.compustore2.tampilan.viewmodel.RegisterViewModel

object DestinasiRegister : DestinasiNavigasi {
    override val route = "register"
    override val titleRes = "Daftar"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanRegister(
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val context = LocalContext.current
    val registerState = viewModel.uiState

    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterUiState.Success -> {
                Toast.makeText(context, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                onRegisterSuccess() // Kembali ke Login
            }
            is RegisterUiState.Error -> {
                Toast.makeText(context, registerState.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(R.string.title_register)) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.nama, onValueChange = { viewModel.nama = it },
                label = { Text("Nama") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.email, onValueChange = { viewModel.email = it },
                label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.noHp, onValueChange = { viewModel.noHp = it },
                label = { Text("No HP") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.password, onValueChange = { viewModel.password = it },
                label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.onRegister() },
                modifier = Modifier.fillMaxWidth(),
                enabled = registerState != RegisterUiState.Loading
            ) {
                if (registerState == RegisterUiState.Loading) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                else Text("Daftar")
            }
        }
    }
}