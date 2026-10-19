package com.akivili.app.domain.model

data class Game(
    val id: String,
    val title: String,
    val publisher: String,
    val category: String,
    val imageUrl: String,
    val bannerUrl: String,
    val requiresServerId: Boolean = false,
    val serverList: List<String> = emptyList(),
    val idPlaceholder: String = "Masukkan User ID",
    val serverPlaceholder: String = "Masukkan Server ID / Zone ID",
    val idHelperGuide: String = "Untuk menemukan User ID, buka profil in-game Anda."
)
