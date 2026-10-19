package com.akivili.app.presentation.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akivili.app.core.designsystem.theme.*
import com.akivili.app.data.dummy.DummyData
import com.akivili.app.domain.model.PaymentCategory
import com.akivili.app.domain.model.PaymentMethod
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    gameId: String,
    packageId: String,
    userId: String,
    serverId: String?,
    onBackClick: () -> Unit,
    onPaymentSuccessProceed: (invoiceId: String) -> Unit
) {
    val game = remember(gameId) {
        DummyData.games.find { it.id == gameId } ?: DummyData.games.first()
    }
    val pkg = remember(gameId, packageId) {
        DummyData.getPackagesForGame(gameId).find { it.id == packageId }
            ?: DummyData.getPackagesForGame(gameId).first()
    }

    var selectedMethod by remember {
        mutableStateOf<PaymentMethod?>(DummyData.paymentMethods.first())
    }

    val rupiahFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
    }

    val adminFee = selectedMethod?.adminFee ?: 0L
    val totalAmount = pkg.price + adminFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Metode Pembayaran", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AkiviliPrimary)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Bayar", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                        Text(
                            text = rupiahFormat.format(totalAmount),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AkiviliPrimaryDark
                            )
                        )
                    }

                    Button(
                        onClick = {
                            val newInvoiceId = "INV-AKV-${System.currentTimeMillis().toString().takeLast(8)}"
                            onPaymentSuccessProceed(newInvoiceId)
                        },
                        enabled = selectedMethod != null,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AkiviliPrimary),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Bayar Sekarang", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AkiviliBackground)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ringkasan Pesanan Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Ringkasan Pesanan",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Game", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                            Text(game.title, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Item Topup", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                            Text(pkg.name, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("ID Akun", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                            Text(
                                if (serverId != null && serverId != "NONE") "$userId ($serverId)" else userId,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Harga", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                            Text(rupiahFormat.format(pkg.price), fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Biaya Admin", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                            Text(rupiahFormat.format(adminFee), fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Pilihan Metode Pembayaran
            item {
                Text(
                    text = "Pilih Metode Pembayaran",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            }

            // Group by category
            val groupedMethods = DummyData.paymentMethods.groupBy { it.category }

            groupedMethods.forEach { (category, methods) ->
                item {
                    Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = AkiviliSecondary
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(methods) { method ->
                    val isSelected = selectedMethod?.code == method.code
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMethod = method },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFFFFFBEB) else Color.White
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) AkiviliPrimary else AkiviliOutline
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    modifier = Modifier.size(36.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) AkiviliPrimaryContainer else AkiviliSurfaceVariant
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.QrCode2,
                                            contentDescription = null,
                                            tint = if (isSelected) AkiviliPrimaryDark else TextSecondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = method.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp
                                        )
                                    )
                                    Text(
                                        text = "+ Biaya ${rupiahFormat.format(method.adminFee)}",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 11.sp,
                                            color = TextMuted
                                        )
                                    )
                                }
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Terpilih",
                                    tint = AkiviliPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
