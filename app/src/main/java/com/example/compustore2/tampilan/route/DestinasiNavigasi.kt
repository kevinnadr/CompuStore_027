package com.example.compustore2.tampilan.route



object DestinasiHome : DestinasiNavigasi {
    override val route = "home"
    override val titleRes = "Compustore ID"
}


// HANYA INI SAJA ISINYA
interface DestinasiNavigasi {
    val route: String
    val titleRes: String // Atau Int jika pakai R.string
}
object DestinasiLogin : DestinasiNavigasi {
    override val route = "login"
    override val titleRes = "Masuk"
}

object DestinasiRegister : DestinasiNavigasi {
    override val route = "register"
    override val titleRes = "Daftar"
}

object DestinasiEntry : DestinasiNavigasi {
    override val route = "item_entry"
    override val titleRes = "Entry Produk"
}

object DestinasiUpdate : DestinasiNavigasi {
    override val route = "update"
    override val titleRes = "Edit Produk"
    const val itemId = "itemId" // Kunci untuk mengambil ID produk
    val routeWithArgs = "$route/{$itemId}"
}

object DestinasiRiwayat : DestinasiNavigasi {
    override val route = "riwayat"
    override val titleRes = "Riwayat Pesanan"
}