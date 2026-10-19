package com.akivili.app.domain.model

enum class PaymentCategory(val displayName: String) {
    QRIS("QRIS"),
    E_WALLET("E-Wallet"),
    VIRTUAL_ACCOUNT("Virtual Account Bank"),
    CONVENIENCE_STORE("Minimarket")
}

data class PaymentMethod(
    val code: String,
    val name: String,
    val category: PaymentCategory,
    val adminFee: Long,
    val iconUrl: String? = null,
    val isAvailable: Boolean = true,
    val instructions: List<String> = emptyList()
)
