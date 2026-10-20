package com.akivili.app.domain.model

data class Voucher(
    val code: String,
    val discountPercent: Int = 0,
    val discountAmount: Long = 0,
    val description: String
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false
)
