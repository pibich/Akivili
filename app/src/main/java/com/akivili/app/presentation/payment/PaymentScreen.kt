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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
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
import com.akivili.app.domain.model.Voucher
import java.text.NumberFormat
import java.util.Locale

import com.akivili.app.core.designsystem.animation.bouncyClick
import com.akivili.app.presentation.components.AkiviliTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    gameId: String,
    packageId: String,
    userId: String,
    serverId: String?,
    appliedVoucher: Voucher?,
    onBackClick: () -> Unit,
    onConfirmOrder: (paymentMethodCode: String) -> Unit
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

    // Calculations
    val subtotal = pkg.price
    val discountAmount = if (appliedVoucher != null) {
        if (appliedVoucher.discountPercent > 0) {
            (subtotal * appliedVoucher.discountPercent) / 100
        } else {
            appliedVoucher.discountAmount
        }
    } else 0L

    val discountedSubtotal = maxOf(0L, subtotal - discountAmount)
    val adminFee = selectedMethod?.adminFee ?: 0L
    val totalAmount = discountedSubtotal + adminFee

    Scaffold(
        topBar = {
            AkiviliTopBar(
                title = "Pilih Metode Pembayaran",
                showBackButton = true,
                onBackClick = onBackClick,
                showNotificationIcon = false
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 14.dp,
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
                        Text(
                            text = "Total Bayar",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontSize = 11.sp)
                        )
                        Text(
                            text = rupiahFormat.format(totalAmount),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AkiviliPrimaryDark,
                                fontSize = 18.sp
                            )
                        )
                    }

                    Button(
                        onClick = {
                            selectedMethod?.let { method ->
                                onConfirmOrder(method.code)
                            }
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
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        SummaryRow("Game", game.title)
                        SummaryRow("Item Topup", pkg.name)
                        SummaryRow(
                            "Akun Tujuan",
                            if (serverId != null && serverId != "NONE") "$userId ($serverId)" else userId
                        )
                        SummaryRow("Harga Paket", rupiahFormat.format(subtotal))

                        if (discountAmount > 0) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Diskon Voucher (${appliedVoucher?.code})",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = StatusSuccess, fontSize = 12.sp)
                                )
                                Text(
                                    "- ${rupiahFormat.format(discountAmount)}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = StatusSuccess,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        SummaryRow("Biaya Admin", rupiahFormat.format(adminFee))

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = AkiviliOutline)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Total Tagihan",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                rupiahFormat.format(totalAmount),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AkiviliPrimaryDark
                                )
                            )
                        }
                    }
                }
            }

            // Heading Pilihan Pembayaran
            item {
                Text(
                    text = "Metode Pembayaran",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 16.sp
                    )
                )
            }

            val groupedMethods = DummyData.paymentMethods.groupBy { it.category }

            groupedMethods.forEach { (category, methods) ->
                item {
                    Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = AkiviliSecondary
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                items(methods) { method ->
                    val isSelected = selectedMethod?.code == method.code
                    val animatedBorderColor by androidx.compose.animation.animateColorAsState(
                        targetValue = if (isSelected) AkiviliPrimary else AkiviliOutline,
                        animationSpec = androidx.compose.animation.core.tween(200),
                        label = "MethodBorder"
                    )
                    val animatedBgColor by androidx.compose.animation.animateColorAsState(
                        targetValue = if (isSelected) Color(0xFFFFFBEB) else Color.White,
                        animationSpec = androidx.compose.animation.core.tween(200),
                        label = "MethodBg"
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bouncyClick { selectedMethod = method },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = animatedBgColor
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 1.8.dp else 1.dp,
                            color = animatedBorderColor
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
                                    modifier = Modifier.size(38.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) AkiviliPrimaryContainer else AkiviliSurfaceVariant
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        val icon = when (method.category) {
                                            PaymentCategory.QRIS -> Icons.Default.QrCode2
                                            PaymentCategory.E_WALLET -> Icons.Default.AccountBalanceWallet
                                            PaymentCategory.VIRTUAL_ACCOUNT -> Icons.Default.AccountBalance
                                            else -> Icons.Default.AccountBalanceWallet
                                        }
                                        Icon(
                                            imageVector = icon,
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
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "Biaya Layanan: ${rupiahFormat.format(method.adminFee)}",
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

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontSize = 12.sp))
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 12.sp))
    }
}
