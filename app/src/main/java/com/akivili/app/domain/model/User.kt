package com.akivili.app.domain.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val points: Int = 0,
    val avatarUrl: String? = null
)
