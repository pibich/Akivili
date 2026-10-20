package com.akivili.app.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akivili.app.core.designsystem.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.akivili.app.presentation.components.AkiviliTopBar

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onSubmitSuccess: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }
    var resendCooldown by remember { mutableIntStateOf(0) }

    LaunchedEffect(resendCooldown) {
        if (resendCooldown > 0) {
            delay(1000)
            resendCooldown--
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AkiviliTopBar(
                title = "Lupa Kata Sandi",
                showBackButton = true,
                onBackClick = onBackClick,
                showNotificationIcon = false
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            if (!isSubmitted) {
                Text(
                    text = "Pemulihan Akun",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Masukkan alamat email terdaftar Anda. Kami akan mengirimkan tautan reset kata sandi dan kode OTP pemulihan.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, textAlign = TextAlign.Center),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Terdaftar") },
                    placeholder = { Text("nama@email.com") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AkiviliPrimary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (email.contains("@") && email.contains(".")) {
                            isSubmitted = true
                            resendCooldown = 60
                            scope.launch {
                                snackbarHostState.showSnackbar("Instruksi pemulihan berhasil dikirim ke $email.")
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Format email tidak valid.")
                            }
                        }
                    },
                    enabled = email.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AkiviliPrimary)
                ) {
                    Text(
                        text = "Kirim Instruksi Reset",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = StatusSuccess,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Instruksi Telah Terkirim!",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Periksa kotak masuk email Anda di $email untuk melanjutkan proses perubahan kata sandi.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, textAlign = TextAlign.Center),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onSubmitSuccess,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AkiviliPrimary)
                ) {
                    Text("Kembali ke Halaman Masuk", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        resendCooldown = 60
                        scope.launch {
                            snackbarHostState.showSnackbar("Email pemulihan telah dikirim ulang.")
                        }
                    },
                    enabled = resendCooldown == 0
                ) {
                    Text(
                        text = if (resendCooldown > 0) "Kirim ulang dalam ${resendCooldown}s" else "Kirim ulang email",
                        color = if (resendCooldown > 0) TextMuted else AkiviliPrimaryDark
                    )
                }
            }
        }
    }
}
