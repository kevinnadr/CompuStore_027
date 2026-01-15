package com.example.compustore2.tampilan.view


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items // <--- INI PENTING YANG BIKIN ERROR SEBELUMNYA
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.compustore2.R
import com.example.compustore2.model.CartItem
import com.example.compustore2.model.Produk
import com.example.compustore2.tampilan.CompustoreTopAppBar
import com.example.compustore2.tampilan.viewmodel.HomeUiState
import com.example.compustore2.tampilan.viewmodel.HomeViewModel
import com.example.compustore2.tampilan.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToCheckout: () -> Unit = {},
    onNavigateToEntry: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var showCartPopup by remember { mutableStateOf(false) }
    val homeState = viewModel.homeUiState
    val cartItems by viewModel.cartItems.collectAsState()
    val totalCartItems = cartItems.sumOf { it.jumlah }

    LaunchedEffect(Unit) { viewModel.getProduk() }

    Scaffold(
        topBar = {
            // TopBar Custom dengan Tombol Keranjang
            CompustoreTopAppBar(
                title = "CompuStore ID",
                canNavigateBack = false,
                actions = {
                    Box(modifier = Modifier.padding(end = 8.dp)) {
                        IconButton(onClick = { showCartPopup = true }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Keranjang",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        // Badge Merah (Angka)
                        if (totalCartItems > 0) {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.White,
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Text(totalCartItems.toString(), fontSize = 10.sp)
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToEntry,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Barang")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            when (homeState) {
                is HomeUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is HomeUiState.Success -> {
                    if (homeState.produk.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Belum ada barang tersedia.", color = Color.Gray)
                        }
                    } else {
                        GridProduk(
                            produkList = homeState.produk,
                            onAddToCart = { viewModel.addToCart(it) },
                            onDetailClick = onNavigateToDetail
                        )
                    }
                }
                is HomeUiState.Error -> {
                    ErrorScreen(message = homeState.message, onRetry = { viewModel.getProduk() })
                }
            }
        }

        // Popup Keranjang
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

// --- KOMPONEN UI ---

@Composable
fun GridProduk(
    produkList: List<Produk>,
    onAddToCart: (Produk) -> Unit,
    onDetailClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner Promo
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            PromoBanner()
        }
        // List Produk
        items(produkList) { produk ->
            ProductCardModern(produk, onAddToCart, onDetailClick)
        }
    }
}

@Composable
fun PromoBanner() {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth().height(150.dp).padding(bottom = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.horizontalGradient(listOf(Color(0xFF6200EE), Color(0xFFBB86FC)))
                )
            )
            Row(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Promo Spesial!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Diskon Laptop Gaming!", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                }
                Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(48.dp))
            }
        }
    }
}

@Composable
fun ProductCardModern(
    produk: Produk,
    onAddToCart: (Produk) -> Unit,
    onDetailClick: (Int) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth().clickable { onDetailClick(produk.id) }
    ) {
        Column {
            Box(modifier = Modifier.height(140.dp).fillMaxWidth()) {
                AsyncImage(
                    model = "http://10.0.2.2:3000/uploads/${produk.gambar ?: ""}",
                    contentDescription = produk.namaProduk,
                    error = painterResource(R.drawable.ic_launcher_background),
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text("Stok: ${produk.stok}", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(produk.namaProduk ?: "Tanpa Nama", maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Rp ${String.format("%,.0f", produk.harga)}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onAddToCart(produk) },
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Beli", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = painterResource(R.drawable.ic_launcher_foreground), contentDescription = null, tint = Color.Red, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Gagal Memuat", fontWeight = FontWeight.Bold)
        Text(text = message, color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Text(" Coba Lagi")
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
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Keranjang Belanja", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                if (cartItems.isEmpty()) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Keranjang Kosong", color = Color.Gray)
                    }
                } else {
                    // LazyColumn menggunakan 'items' dari androidx.compose.foundation.lazy.items
                    androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.weight(1f)) {
                        items(cartItems) { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = "http://10.0.2.2:3000/uploads/${item.produk.gambar ?: ""}",
                                    contentDescription = null, modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop,
                                    error = painterResource(R.drawable.ic_launcher_background)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.produk.namaProduk ?: "Item", fontWeight = FontWeight.Bold, maxLines = 1)
                                    Text("Rp ${String.format("%,.0f", item.produk.harga)}", fontSize = 12.sp, color = Color.Gray)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { onRemove(item.produk) }, modifier = Modifier.size(24.dp)) {
                                        Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    }
                                    Text("${item.jumlah}", modifier = Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold)
                                    IconButton(onClick = { onAdd(item.produk) }, modifier = Modifier.size(24.dp)) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                            Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total:", style = MaterialTheme.typography.titleMedium)
                    Text("Rp ${String.format("%,.0f", totalPrice)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth(), enabled = cartItems.isNotEmpty(), shape = RoundedCornerShape(8.dp)) {
                    Text("Checkout")
                }
            }
        }
    }
}