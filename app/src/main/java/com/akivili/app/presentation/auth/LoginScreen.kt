package com.akivili.app.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akivili.app.core.designsystem.theme.*
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLogin: (email: String, pass: String) -> Boolean,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onContinueAsGuest: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var emailOrUsername by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Logo & Header
            Text(
                text = "Akivili.",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AkiviliPrimary
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Masuk ke Akun Anda",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Email or Username
            OutlinedTextField(
                value = emailOrUsername,
                onValueChange = {
                    emailOrUsername = it
                    isError = false
                },
                label = { Text("Email atau Username") },
                placeholder = { Text("nama@email.com") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = AkiviliPrimary)
                },
                isError = isError && emailOrUsername.isBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isError = false
                },
                label = { Text("Kata Sandi") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = AkiviliPrimary)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Kata Sandi"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = isError && password.isBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Lupa Password Link
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Lupa Password?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = AkiviliPrimaryDark,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.clickable { onNavigateToForgotPassword() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Masuk
            Button(
                onClick = {
                    if (emailOrUsername.isBlank() || password.isBlank()) {
                        isError = true
                        scope.launch {
                            snackbarHostState.showSnackbar("Harap isi email/username dan kata sandi Anda.")
                        }
                    } else {
                        val success = onLogin(emailOrUsername, password)
                        if (success) {
                            onLoginSuccess()
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Gagal masuk. Periksa kembali data Anda.")
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AkiviliPrimary)
            ) {
                Text(
                    text = "Masuk",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Guest Mode Option
            OutlinedButton(
                onClick = onContinueAsGuest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, AkiviliOutline)
            ) {
                Text(
                    text = "Lanjutkan sebagai Tamu (Guest)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Register Link
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Belum punya akun? ",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )
                Text(
                    text = "Daftar Sekarang",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = AkiviliPrimaryDark,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}
