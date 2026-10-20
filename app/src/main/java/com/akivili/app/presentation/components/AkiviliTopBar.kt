package com.akivili.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akivili.app.core.designsystem.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AkiviliTopBar(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    showNotificationIcon: Boolean = true,
    unreadNotificationCount: Int = 0,
    onNotificationClick: () -> Unit = {}
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = if (title == "Akivili.") FontWeight.ExtraBold else FontWeight.Bold,
                        fontSize = if (title == "Akivili.") 22.sp else 18.sp,
                        color = if (title == "Akivili.") AkiviliPrimary else TextPrimary
                    )
                )
            },
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextPrimary
                        )
                    }
                }
            },
            actions = {
                if (showNotificationIcon) {
                    IconButton(onClick = onNotificationClick) {
                        if (unreadNotificationCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = StatusFailed,
                                        contentColor = Color.White
                                    ) {
                                        Text("$unreadNotificationCount", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifikasi",
                                    tint = TextPrimary
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifikasi",
                                tint = TextPrimary
                            )
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                scrolledContainerColor = Color.White
            )
        )
        HorizontalDivider(color = AkiviliOutline.copy(alpha = 0.5f), thickness = 0.8.dp)
    }
}
