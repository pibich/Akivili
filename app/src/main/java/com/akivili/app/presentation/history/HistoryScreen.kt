package com.akivili.app.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akivili.app.core.designsystem.animation.bouncyClick
import com.akivili.app.core.designsystem.theme.*
import com.akivili.app.domain.model.Transaction
import com.akivili.app.domain.model.TransactionStatus
import com.akivili.app.presentation.components.AkiviliTopBar
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    transactions: List<Transaction>,
    onTransactionClick: (String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Menunggu", "Berhasil", "Gagal")

    val rupiahFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
    }

    val dateFormat = remember {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    }

    val filteredList = remember(selectedFilter, transactions) {
        when (selectedFilter) {
            "Menunggu" -> transactions.filter { it.status == TransactionStatus.PENDING }
            "Berhasil" -> transactions.filter { it.status == TransactionStatus.SUCCESS }
            "Gagal" -> transactions.filter { it.status == TransactionStatus.FAILED || it.status == TransactionStatus.EXPIRED }
            else -> transactions
        }
    }

    Scaffold(
        topBar = {
            AkiviliTopBar(title = "Riwayat Transaksi", showBackButton = false, showNotificationIcon = false)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AkiviliBackground)
                .padding(paddingValues)
        ) {
            // Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AkiviliPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) AkiviliPrimary else AkiviliOutline
                        )
                    )
                }
            }

            // Transactions List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tidak Ada Riwayat Transaksi",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Transaksi yang Anda lakukan akan tercatat di sini.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontSize = 12.sp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredList, key = { it.invoiceId }) { trx ->
                        TransactionItemCard(
                            transaction = trx,
                            rupiahFormat = rupiahFormat,
                            dateFormat = dateFormat,
                            onClick = { onTransactionClick(trx.invoiceId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItemCard(
    transaction: Transaction,
    rupiahFormat: NumberFormat,
    dateFormat: SimpleDateFormat,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .bouncyClick { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = transaction.gameTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
                )

                // Status Badge
                val (badgeBg, badgeText, statusColor) = when (transaction.status) {
                    TransactionStatus.SUCCESS -> Triple(StatusSuccessContainer, "Berhasil", StatusSuccess)
                    TransactionStatus.PENDING -> Triple(StatusPendingContainer, "Menunggu", StatusPending)
                    TransactionStatus.FAILED -> Triple(StatusFailedContainer, "Gagal", StatusFailed)
                    TransactionStatus.EXPIRED -> Triple(StatusFailedContainer, "Kedaluwarsa", StatusFailed)
                }

                Surface(
                    color = badgeBg,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = statusColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${transaction.packageName} • ID: ${transaction.userId}${if (transaction.serverId != null) " (${transaction.serverId})" else ""}",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontSize = 12.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = AkiviliOutline)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = transaction.invoiceId,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = dateFormat.format(Date(transaction.createdAt)),
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted, fontSize = 10.sp)
                    )
                }

                Text(
                    text = rupiahFormat.format(transaction.totalAmount),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AkiviliPrimaryDark,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}
