package com.example.compustore2.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.compustore2.R
import com.example.compustore2.model.CartItem
import com.example.compustore2.model.Produk
import com.example.compustore2.tampilan.route.DestinasiNavigasi
import com.example.compustore2.tampilan.viewmodel.HomeUiState
import com.example.compustore2.tampilan.viewmodel.HomeViewModel
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel

object DestinasiHome : DestinasiNavigasi {
    override val route = "home"
    override val titleRes = "Compustore"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToCheckout: () -> Unit = {},
    onNavigateToEntry: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {},
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var showLoginDialog by remember { mutableStateOf(false) }
    var showCartPopup by remember { mutableStateOf(false) }

    val homeState = viewModel.homeUiState
    val cartItems by viewModel.cartItems.collectAsState()
    val totalCartItems = cartItems.sumOf { it.jumlah }

    val isAdmin = true

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Compustore ID") },
                actions = {
                    BadgedBox(
                        badge = {
                            if (totalCartItems > 0) {
                                Badge { Text(totalCartItems.toString()) }
                            }
                        }
                    ) {
                        IconButton(onClick = {
                            if (viewModel.isUserLoggedIn()) {
                                showCartPopup = true
                            } else {
                                showLoginDialog = true
                            }
                        }) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Keranjang")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = onNavigateToEntry,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Barang")
                }
            }
        }
    ) { innerPadding ->

        when (homeState) {
            is HomeUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is HomeUiState.Success -> {
                if (homeState.produk.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Belum ada barang tersedia") }
                } else {
                    ListProduk(
                        produkList = homeState.produk,
                        onAddToCart = { produk ->
                            if (viewModel.isUserLoggedIn()) {
                                viewModel.addToCart(produk)
                            } else {
                                showLoginDialog = true
                            }
                        },
                        onDetailClick = onNavigateToDetail,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
            is HomeUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Gagal memuat data / Server Error") }
        }

        if (showLoginDialog) {
            AlertDialog(
                onDismissRequest = { showLoginDialog = false },
                title = { Text("Belum Login") },
                text = { Text("Silakan login untuk berbelanja.") },
                confirmButton = {
                    Button(onClick = { showLoginDialog = false; onNavigateToLogin() }) { Text("Login") }
                },
                dismissButton = {
                    TextButton(onClick = { showLoginDialog = false }) { Text("Batal") }
                }
            )
        }

        if (showCartPopup) {
            CartPopup(
                cartItems = cartItems,
                totalPrice = viewModel.getTotalCartPrice(),
                onDismiss = { showCartPopup = false },
                onCheckout = {
                    showCartPopup = false
                    onNavigateToCheckout()
                },
                onAdd = { viewModel.addToCart(it) },
                onRemove = { viewModel.removeFromCart(it.id) }
            )
        }
    }
}

@Composable
fun ListProduk(
    produkList: List<Produk>,
    onAddToCart: (Produk) -> Unit,
    onDetailClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(produkList) { produk ->
            CardProduk(
                produk = produk,
                onAddToCart = onAddToCart,
                onDetailClick = onDetailClick
            )
        }
    }
}

@Composable
fun CardProduk(
    produk: Produk,
    onAddToCart: (Produk) -> Unit,
    onDetailClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onDetailClick(produk.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Gambar Produk
            AsyncImage(
                model = "http://10.0.2.2:3000/uploads/${produk.gambar ?: ""}", // Tambahkan ?: ""
                contentDescription = produk.namaProduk ?: "Produk", // Tambahkan ?: "Produk"
                error = painterResource(R.drawable.ic_launcher_background),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {
                // PERBAIKAN DI SINI (Tambahkan ?: "...")
                Text(
                    text = produk.namaProduk ?: "Nama Tidak Tersedia",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Rp ${String.format("%,.0f", produk.harga)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Stok: ${produk.stok}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onAddToCart(produk) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tambah Keranjang")
                }
            }
        }
    }
}

@Composable
fun CartPopup(
    cartItems: List<CartItem>,
    totalPrice: Double,
    onDismiss: () -> Unit,
    onCheckout: () -> Unit,
    onAdd: (Produk) -> Unit,
    onRemove: (Produk) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp, max = 500.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Keranjang Belanja", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                if (cartItems.isEmpty()) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Keranjang Kosong")
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(cartItems) { item ->
                            CartItemRow(item, onAdd, onRemove)
                            Divider()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total:", style = MaterialTheme.typography.titleMedium)
                    Text("Rp ${String.format("%,.0f", totalPrice)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth(), enabled = cartItems.isNotEmpty()) {
                    Text("Checkout")
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onAdd: (Produk) -> Unit, onRemove: (Produk) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = "http://10.0.2.2:3000/uploads/${item.produk.gambar ?: ""}",
            contentDescription = null, modifier = Modifier.size(60.dp), contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_launcher_background)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            // PERBAIKAN DI SINI JUGA
            Text(
                text = item.produk.namaProduk ?: "Produk",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
            Text("Rp ${item.produk.harga}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onRemove(item.produk) }, modifier = Modifier.size(30.dp)) {
                Text("-", style = MaterialTheme.typography.titleLarge)
            }
            Text("${item.jumlah}", modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { onAdd(item.produk) }, modifier = Modifier.size(30.dp)) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    }
}