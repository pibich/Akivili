package com.akivili.app.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AkiviliPrimary,
    onPrimary = TextOnOrange,
    primaryContainer = AkiviliPrimaryContainer,
    onPrimaryContainer = AkiviliOnPrimaryContainer,
    secondary = AkiviliSecondary,
    onSecondary = TextOnOrange,
    background = AkiviliBackground,
    onBackground = TextPrimary,
    surface = AkiviliSurface,
    onSurface = TextPrimary,
    surfaceVariant = AkiviliSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = AkiviliOutline,
    error = StatusFailed,
    onError = TextOnOrange
)

@Composable
fun AkiviliTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.BLACK
            window.navigationBarColor = android.graphics.Color.BLACK
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AkiviliTypography,
        content = content
    )
}
