package com.akivili.app.presentation.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akivili.app.core.designsystem.theme.*
import com.akivili.app.domain.model.Transaction
import com.akivili.app.domain.model.TransactionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

import com.akivili.app.core.designsystem.animation.bouncyClick
import com.akivili.app.presentation.components.AkiviliTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRISScreen(
    invoiceId: String,
    transaction: Transaction?,
    onMarkPaid: (String) -> Unit,
    onBackToHome: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    var isCheckingStatus by remember { mutableStateOf(false) }
    var isPaymentSuccess by remember { mutableStateOf(transaction?.status == TransactionStatus.SUCCESS) }
    var timeLeftSeconds by remember { mutableIntStateOf(895) } // ~15:00

    val rupiahFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
    }

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
            AkiviliTopBar(
                title = "Tagihan Pembayaran",
                showBackButton = true,
                onBackClick = onBackToHome,
                showNotificationIcon = false
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AkiviliBackground)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status Timer or Success Banner
            if (isPaymentSuccess) {
                Surface(
                    color = StatusSuccessContainer,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, StatusSuccess),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "PEMBAYARAN LUNAS & BERHASIL",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StatusSuccess,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = "Diamond/item sedang otomatis dikirim ke akun Anda.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontSize = 11.sp)
                            )
                        }
                    }
                }
            } else {
                Surface(
                    color = StatusPendingContainer,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, StatusPending.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = StatusPending, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Batas Waktu Pembayaran: ",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontSize = 12.sp)
                        )
                        Text(
                            text = formattedTime,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = StatusFailed,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // QR Code Box (If not paid) or Success Receipt
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (transaction != null) transaction.paymentMethodName else "QRIS Standar Indonesia",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AkiviliSecondary,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = "Scan kode QR ini menggunakan m-Banking atau E-Wallet pilihan Anda",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 11.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                    )

                    // Realistic QR Code Box
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .background(Color(0xFFFAFAFA), RoundedCornerShape(12.dp))
                            .border(1.5.dp, if (isPaymentSuccess) StatusSuccess else AkiviliOutline, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "Kode QR",
                            tint = if (isPaymentSuccess) StatusSuccess else AkiviliSecondary,
                            modifier = Modifier.size(190.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Kode QR berhasil disimpan ke galeri foto.")
                            }
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Unduh QR Code", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                        Text("Nomor Tagihan", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontSize = 12.sp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(invoiceId, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(invoiceId))
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Nomor tagihan disalin: $invoiceId")
                                    }
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Salin", tint = AkiviliPrimary, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider(color = AkiviliOutline)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (transaction != null) {
                        SummaryRow("Game", transaction.gameTitle)
                        SummaryRow("Item", transaction.packageName)
                        SummaryRow("User ID", if (transaction.serverId != null) "${transaction.userId} (${transaction.serverId})" else transaction.userId)
                        SummaryRow("Metode", transaction.paymentMethodName)

                        Spacer(modifier = Modifier.height(6.dp))
                        HorizontalDivider(color = AkiviliOutline)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Bayar", fontWeight = FontWeight.Bold)
                            Text(
                                rupiahFormat.format(transaction.totalAmount),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AkiviliPrimaryDark,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            if (!isPaymentSuccess) {
                Button(
                    onClick = {
                        isCheckingStatus = true
                        scope.launch {
                            delay(1200) // Simulates server verification
                            isCheckingStatus = false
                            isPaymentSuccess = true
                            onMarkPaid(invoiceId)
                            snackbarHostState.showSnackbar("Selamat! Pembayaran berhasil diverifikasi.")
                        }
                    },
                    enabled = !isCheckingStatus,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AkiviliPrimary)
                ) {
                    if (isCheckingStatus) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Memverifikasi...")
                    } else {
                        Text("Cek Status Pembayaran", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Button(
                    onClick = onBackToHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess)
                ) {
                    Text("✓ Selesai & Kembali ke Beranda", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!isPaymentSuccess) {
                TextButton(
                    onClick = onBackToHome,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Bayar Nanti (Kembali ke Beranda)", color = TextSecondary, fontSize = 12.sp)
                }
            }
        }
    }
}
