package com.example.compustore2.tampilan.route


object DestinasiDetail : DestinasiNavigasi {
    override val route = "detail_produk"
    override val titleRes = "Detail Produk"

    // TAMBAHKAN INI AGAR TIDAK ERROR "Unresolved reference 'produkId'"
    const val produkId = "itemId"
    val routeWithArgs = "$route/{$produkId}"
}