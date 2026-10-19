package com.akivili.app.domain.model

data class TopupPackage(
    val id: String,
    val gameId: String,
    val name: String,
    val price: Long,
    val bonus: String? = null,
    val isPopular: Boolean = false,
    val iconUrl: String? = null
)
