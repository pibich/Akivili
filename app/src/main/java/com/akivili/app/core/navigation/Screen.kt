package com.akivili.app.core.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    
    // Bottom Nav Tabs
    object Home : Screen("home")
    object History : Screen("history")
    object HelpCenter : Screen("help_center")
    object Profile : Screen("profile")
    
    // Topup & Checkout Flow
    object Topup : Screen("topup/{gameId}") {
        fun createRoute(gameId: String) = "topup/$gameId"
    }
    object Payment : Screen("payment/{gameId}/{packageId}/{userId}?serverId={serverId}") {
        fun createRoute(gameId: String, packageId: String, userId: String, serverId: String?): String {
            val safeServer = serverId?.ifBlank { "NONE" } ?: "NONE"
            return "payment/$gameId/$packageId/$userId?serverId=$safeServer"
        }
    }
    object QRISInvoice : Screen("qris/{invoiceId}") {
        fun createRoute(invoiceId: String) = "qris/$invoiceId"
    }
}
