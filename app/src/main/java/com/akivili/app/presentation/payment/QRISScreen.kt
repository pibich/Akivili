package com.akivili.app.presentation.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akivili.app.core.designsystem.animation.bouncyClick
import com.akivili.app.core.designsystem.theme.*
import com.akivili.app.domain.model.Transaction
import com.akivili.app.domain.model.TransactionStatus
import com.akivili.app.presentation.components.AkiviliTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

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
    var timeLeftSeconds by remember { mutableIntStateOf(892) } // ~14:52
    var isCopied by remember { mutableStateOf(false) }

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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AkiviliTopBar(
                title = "Tagihan Pembayaran",
                showBackButton = true,
                onBackClick = onBackToHome,
                showNotificationIcon = false
            )
        },
        bottomBar = {
            // Fixed bottom bar: guarantees buttons are ALWAYS visible and never clipped!
            Surface(
                color = Color.White,
                shadowElevation = 14.dp,
                border = BorderStroke(0.8.dp, AkiviliOutline.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!isPaymentSuccess) {
                        Button(
                            onClick = {
                                isCheckingStatus = true
                                scope.launch {
                                    delay(1200) // Simulates server payment verification
                                    isCheckingStatus = false
                                    isPaymentSuccess = true
                                    onMarkPaid(invoiceId)
                                    snackbarHostState.showSnackbar("Selamat! Pembayaran berhasil diverifikasi.")
                                }
                            },
                            enabled = !isCheckingStatus,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .bouncyClick {
                                    if (!isCheckingStatus) {
                                        isCheckingStatus = true
                                        scope.launch {
                                            delay(1200)
                                            isCheckingStatus = false
                                            isPaymentSuccess = true
                                            onMarkPaid(invoiceId)
                                            snackbarHostState.showSnackbar("Selamat! Pembayaran berhasil diverifikasi.")
                                        }
                                    }
                                },
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
                                Text("Memverifikasi Transaksi...", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                            } else {
                                Text("Cek Status Pembayaran", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        TextButton(
                            onClick = onBackToHome,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Bayar Nanti (Kembali ke Beranda)",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Button(
                            onClick = onBackToHome,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .bouncyClick { onBackToHome() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Selesai & Kembali ke Beranda", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AkiviliBackground)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Status Banner (Countdown or Success)
            if (isPaymentSuccess) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = BorderStroke(1.dp, Color(0xFF86EFAC)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDCFCE7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = StatusSuccess,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "PEMBAYARAN LUNAS & BERHASIL",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D),
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = "Pesanan sedang dikirim otomatis ke akun game Anda.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF166534),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            } else {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Selesaikan pembayaran dalam:",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF78350F),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Surface(
                            color = Color(0xFFFEF2F2),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFFECACA))
                        ) {
                            Text(
                                text = formattedTime,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFDC2626),
                                    fontSize = 13.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // 2. QRIS Code Box (or Success Celebration Card)
            if (!isPaymentSuccess) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // QRIS Badge Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color(0xFF0F172A),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "QRIS",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "NMID: ID1020268434454",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMuted,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Compact QR Code Container with authentic border
                        Box(
                            modifier = Modifier
                                .size(175.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFAFAFA))
                                .border(1.2.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode2,
                                contentDescription = "Kode QRIS",
                                tint = Color(0xFF0F172A),
                                modifier = Modifier.size(150.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Scan QRIS menggunakan aplikasi pembayaran pilihan Anda:",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "BCA • Mandiri • BRI • BNI • GoPay • OVO • DANA • ShopeePay",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Kode QR berhasil disimpan ke galeri foto Anda.")
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, AkiviliOutline),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(15.dp), tint = TextSecondary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Unduh QR Code", fontSize = 11.5.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // 3. Rincian Tagihan & Informasi Pesanan
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
                        Text(
                            text = "Rincian Pesanan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 14.sp
                            )
                        )

                        Surface(
                            color = AkiviliSurfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable {
                                        clipboardManager.setText(AnnotatedString(invoiceId))
                                        isCopied = true
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Nomor tagihan disalin: $invoiceId")
                                            delay(2000)
                                            isCopied = false
                                        }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = invoiceId,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                                    contentDescription = "Salin",
                                    tint = if (isCopied) StatusSuccess else AkiviliPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = AkiviliOutline.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(10.dp))

                    if (transaction != null) {
                        InvoiceDetailRow(label = "Game", value = transaction.gameTitle)
                        InvoiceDetailRow(label = "Item Paket", value = transaction.packageName)
                        InvoiceDetailRow(
                            label = "User ID",
                            value = if (transaction.serverId != null) "${transaction.userId} (${transaction.serverId})" else transaction.userId
                        )
                        InvoiceDetailRow(label = "Metode Pembayaran", value = transaction.paymentMethodName)

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = AkiviliOutline.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Pembayaran",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = rupiahFormat.format(transaction.totalAmount),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AkiviliPrimaryDark,
                                    fontSize = 17.sp
                                )
                            )
                        }
                    }
                }
            }

            // 4. Garansi Keamanan
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Transaksi dijamin aman & terenkripsi otomatis 256-bit SSL",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted,
                        fontSize = 10.5.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun InvoiceDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondary,
                fontSize = 12.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.5.sp,
                textAlign = TextAlign.End
            ),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
