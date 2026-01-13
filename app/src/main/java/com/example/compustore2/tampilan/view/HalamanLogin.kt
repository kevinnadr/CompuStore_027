package com.example.compustore2.tampilan.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compustore2.R
import com.example.compustore2.tampilan.route.DestinasiNavigasi
import com.example.compustore2.tampilan.viewmodel.LoginUiState
import com.example.compustore2.tampilan.viewmodel.LoginViewModel
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel


object DestinasiLogin : DestinasiNavigasi {
    override val route = "login"
    override val titleRes = "Masuk"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanLogin(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val context = LocalContext.current
    val loginState = viewModel.uiState

    // Cek status Login
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginUiState.Success -> {
                Toast.makeText(context, "Selamat Datang, ${java.lang.String.valueOf(loginState.user?.nama)}!", Toast.LENGTH_SHORT).show()
                onLoginSuccess() // Pindah ke Home
                viewModel.resetState()
            }
            is LoginUiState.Error -> {
                Toast.makeText(context, java.lang.String.valueOf(loginState.message), Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_login)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Field Email
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text(stringResource(R.string.label_email)) },
                placeholder = { Text(stringResource(R.string.hint_email)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

            // Field Password
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text(stringResource(R.string.label_password)) },
                placeholder = { Text(stringResource(R.string.hint_password)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

            // Tombol Login
            Button(
                onClick = { viewModel.onLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.button_height)),
                enabled = loginState != LoginUiState.Loading // Disable saat loading
            ) {
                if (loginState == LoginUiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_size))
                    )
                } else {
                    Text(stringResource(R.string.btn_login))
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            // Tombol ke Register
            TextButton(onClick = onNavigateToRegister) {
                Text(stringResource(R.string.text_no_account))
            }
        }
    }
}