package com.akivili.app.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akivili.app.core.designsystem.theme.*
import com.akivili.app.core.navigation.Screen

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home.route, "Beranda", Icons.Default.Home)
    object History : BottomNavItem(Screen.History.route, "Riwayat", Icons.AutoMirrored.Filled.ReceiptLong)
    object Help : BottomNavItem(Screen.HelpCenter.route, "Bantuan", Icons.AutoMirrored.Filled.HelpOutline)
    object Profile : BottomNavItem(Screen.Profile.route, "Profil", Icons.Default.Person)
}

/**
 * Freud v3 Style Floating Capsule Tab Navigation Bar:
 * - Floating Island Pill Dock with smooth rounded curvature (CircleShape)
 * - Active tab expands into high-contrast solid pill with spring physics
 * - Ambient drop shadow and subtle border for premium modern aesthetic
 */
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    spotColor = Color(0x38000000),
                    ambientColor = Color(0x1A000000)
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFE2E8F0),
                    shape = CircleShape
                ),
            shape = CircleShape,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route

                    // Icon spring bounce animation
                    val iconScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.12f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "freudIconScale"
                    )

                    // Active pill container background
                    val pillBgColor by animateColorAsState(
                        targetValue = if (isSelected) AkiviliPrimary else Color.Transparent,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "freudPillBgColor"
                    )

                    // Active content color (white on active pill, muted on inactive)
                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else TextMuted,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "freudContentColor"
                    )

                    Box(
                        modifier = Modifier
                            .height(44.dp)
                            .clip(CircleShape)
                            .background(pillBgColor)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (!isSelected) {
                                    onNavigate(item.route)
                                }
                            }
                            .padding(horizontal = if (isSelected) 16.dp else 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = contentColor,
                                modifier = Modifier
                                    .size(20.dp)
                                    .graphicsLayer {
                                        scaleX = iconScale
                                        scaleY = iconScale
                                    }
                            )

                            AnimatedVisibility(
                                visible = isSelected,
                                enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                                        expandHorizontally(spring(stiffness = Spring.StiffnessMediumLow)),
                                exit = fadeOut(spring(stiffness = Spring.StiffnessMediumLow)) +
                                        shrinkHorizontally(spring(stiffness = Spring.StiffnessMediumLow))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = item.label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = contentColor,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
