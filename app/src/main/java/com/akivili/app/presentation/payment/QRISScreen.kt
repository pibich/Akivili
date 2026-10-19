package com.akivili.app.presentation.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akivili.app.core.designsystem.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRISScreen(
    invoiceId: String,
    onBackToHome: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var timeLeftSeconds by remember { mutableIntStateOf(900) } // 15:00
    var isPaymentSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = timeLeftSeconds) {
        if (timeLeftSeconds > 0 && !isPaymentSuccess) {
            delay(1000)
            timeLeftSeconds--
        }
    }

    val minutes = timeLeftSeconds / 60
    val seconds = timeLeftSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Tagihan Pembayaran", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackToHome) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AkiviliPrimary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AkiviliBackground)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Countdown Timer Card
            Surface(
                color = StatusPendingContainer,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StatusPending.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = StatusPending)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Selesaikan pembayaran dalam: ",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                    )
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StatusFailed
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // QR Code Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "QRIS Dinamis Akivili",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AkiviliSecondary
                        )
                    )
                    Text(
                        text = "Bisa discan dengan seluruh aplikasi E-Wallet & Mobile Banking",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 11.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    // Simulated QRIS Box
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
                            .border(1.5.dp, AkiviliOutline, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "QRIS Code",
                            tint = AkiviliSecondary,
                            modifier = Modifier.size(190.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Kode QR berhasil diunduh ke galeri.")
                            }
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Unduh Gambar QR")
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Details Invoice Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Nomor Pesanan", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(invoiceId, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Nomor pesanan disalin ke papan klip.")
                                    }
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Salin", modifier = Modifier.size(14.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = AkiviliOutline)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Metode Pembayaran", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                        Text("QRIS (Instan)", fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Status Pembayaran", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                        Surface(
                            color = if (isPaymentSuccess) StatusSuccessContainer else StatusPendingContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (isPaymentSuccess) "LUNAS / BERHASIL" else "MENUNGGU PEMBAYARAN",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (isPaymentSuccess) StatusSuccess else StatusPending,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Button(
                onClick = {
                    isPaymentSuccess = true
                    scope.launch {
                        snackbarHostState.showSnackbar("Pembayaran Berhasil! Item top-up sedang otomatis dikirim.")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPaymentSuccess) StatusSuccess else AkiviliPrimary
                )
            ) {
                Text(
                    text = if (isPaymentSuccess) "✓ Pembayaran Berhasil Terverifikasi" else "Cek Status Pembayaran",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembali ke Beranda", color = TextSecondary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
