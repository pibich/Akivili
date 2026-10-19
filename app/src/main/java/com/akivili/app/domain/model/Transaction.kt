package com.akivili.app.domain.model

enum class TransactionStatus(val displayName: String) {
    PENDING("Menunggu Pembayaran"),
    SUCCESS("Berhasil"),
    FAILED("Gagal"),
    EXPIRED("Kedaluwarsa")
}

data class Transaction(
    val invoiceId: String,
    val gameId: String,
    val gameTitle: String,
    val packageName: String,
    val userId: String,
    val serverId: String? = null,
    val paymentMethodName: String,
    val paymentMethodCode: String,
    val subtotal: Long,
    val adminFee: Long,
    val totalAmount: Long,
    val status: TransactionStatus,
    val createdAt: Long,
    val expiresAt: Long,
    val qrisPayload: String? = null
)
