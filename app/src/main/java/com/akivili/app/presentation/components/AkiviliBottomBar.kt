package com.akivili.app.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.akivili.app.core.designsystem.theme.AkiviliPrimary
import com.akivili.app.core.designsystem.theme.TextMuted
import com.akivili.app.core.navigation.Screen

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home.route, "Beranda", Icons.Default.Home)
    object History : BottomNavItem(Screen.History.route, "Riwayat", Icons.Default.ReceiptLong)
    object Help : BottomNavItem(Screen.HelpCenter.route, "Bantuan", Icons.Default.HelpOutline)
    object Profile : BottomNavItem(Screen.Profile.route, "Profil", Icons.Default.Person)
}

@Composable
fun AkiviliBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.History,
        BottomNavItem.Help,
        BottomNavItem.Profile
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        onNavigate(item.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AkiviliPrimary,
                    selectedTextColor = AkiviliPrimary,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor = Color(0xFFFFF3E0)
                )
            )
        }
    }
}
