package com.akivili.app.presentation.help

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akivili.app.core.designsystem.theme.*
import com.akivili.app.presentation.components.AkiviliTopBar

data class FAQItem(val question: String, val answer: String)

@Composable
fun HelpCenterScreen() {
    val context = LocalContext.current

    val faqList = listOf(
        FAQItem(
            "Berapa lama proses pengisian top-up?",
            "Proses pengisian berjalan secara otomatis dan instan dalam waktu 1 - 5 menit setelah pembayaran berhasil diverifikasi oleh sistem kami."
        ),
        FAQItem(
            "Bagaimana cara pembayaran menggunakan QRIS?",
            "Cukup buka aplikasi e-wallet (GoPay, DANA, OVO, ShopeePay) atau mobile banking Anda, pilih menu Scan QRIS, lalu arahkan kamera ke kode QR yang tampil di layar tagihan."
        ),
        FAQItem(
            "Bagaimana jika salah memasukkan User ID / Server ID?",
            "Harap teliti sebelum melakukan pembayaran. Jika transaksi sudah terproses ke akun yang salah, transaksi tidak dapat dibatalkan. Namun jika terjadi kendala, segera hubungi customer service kami."
        ),
        FAQItem(
            "Apakah top-up di Akivili aman dan legal?",
            "100% aman dan legal! Semua nominal dan voucher didapatkan langsung dari distributor resmi dan publisher game."
        )
    )

    Scaffold(
        topBar = {
            AkiviliTopBar(title = "Pusat Bantuan & FAQ", showBackButton = false)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AkiviliBackground)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // CS Contact Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AkiviliPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Butuh Bantuan Langsung?",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Customer Service Akivili siap melayani 24/7",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f))
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/6281234567890?text=Halo%20Akivili,%20saya%20butuh%20bantuan%20terkait%20transaksi"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                contentDescription = null,
                                tint = AkiviliPrimaryDark,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Hubungi WhatsApp CS",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AkiviliPrimaryDark
                                )
                            )
                        }
                    }
                }
            }

            // Section FAQ
            item {
                Text(
                    text = "Pertanyaan Umum (FAQ)",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(faqList) { faq ->
                FAQCard(faq = faq)
            }
        }
    }
}

@Composable
fun FAQCard(faq: FAQItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faq.question,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = TextMuted
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = AkiviliOutline)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = faq.answer,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }
    }
}
