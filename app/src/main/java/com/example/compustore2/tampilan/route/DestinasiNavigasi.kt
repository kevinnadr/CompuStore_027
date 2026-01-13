package com.example.compustore2.tampilan.route


interface DestinasiNavigasi {
    val route: String
    val titleRes: String
}

object DestinasiHome : DestinasiNavigasi {
    override val route = "home"
    override val titleRes = "Compustore ID"
}

object DestinasiDetail : DestinasiNavigasi {
    override val route = "detail"
    override val titleRes = "Detail Produk"
    const val produkId = "itemId"
    val routeWithArgs = "$route/{$produkId}"
}

object DestinasiLogin : DestinasiNavigasi {
    override val route = "login"
    override val titleRes = "Masuk"
}

object DestinasiRegister : DestinasiNavigasi {
    override val route = "register"
    override val titleRes = "Daftar"
}