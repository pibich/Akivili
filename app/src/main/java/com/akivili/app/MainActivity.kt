package com.akivili.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.akivili.app.core.designsystem.theme.AkiviliTheme
import com.akivili.app.presentation.AkiviliApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AkiviliTheme {
                AkiviliApp()
            }
        }
    }
}
