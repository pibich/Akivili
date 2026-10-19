package com.akivili.app.presentation.topup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.akivili.app.core.designsystem.theme.*
import com.akivili.app.data.dummy.DummyData
import com.akivili.app.domain.model.Game
import com.akivili.app.domain.model.TopupPackage
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopupScreen(
    gameId: String,
    onBackClick: () -> Unit,
    onProceedToPayment: (gameId: String, packageId: String, userId: String, serverId: String?) -> Unit
) {
    val game = remember(gameId) {
        DummyData.games.find { it.id == gameId } ?: DummyData.games.first()
    }
    val packages = remember(gameId) {
        DummyData.getPackagesForGame(gameId)
    }

    var userId by remember { mutableStateOf("") }
    var serverId by remember { mutableStateOf("") }
    var selectedPackage by remember { mutableStateOf<TopupPackage?>(null) }
    var promoCode by remember { mutableStateOf("") }

    val isFormValid = userId.isNotBlank() && selectedPackage != null

    val rupiahFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(game.title, color = Color.White, fontWeight = FontWeight.Bold) },
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
                        Text("Total Pembayaran", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary))
                        Text(
                            text = if (selectedPackage != null) rupiahFormat.format(selectedPackage!!.price) else "Rp 0",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AkiviliPrimaryDark
                            )
                        )
                    }

                    Button(
                        onClick = {
                            selectedPackage?.let { pkg ->
                                onProceedToPayment(game.id, pkg.id, userId, serverId.ifBlank { null })
                            }
                        },
                        enabled = isFormValid,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AkiviliPrimary,
                            disabledContainerColor = AkiviliOutline
                        ),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Beli Sekarang", fontWeight = FontWeight.Bold)
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
            // Game Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = game.imageUrl,
                            contentDescription = game.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = game.title,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = game.publisher,
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                color = AkiviliPrimaryContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Proses Instan & Aman",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = AkiviliPrimaryDark,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Step 1: Input Data Akun
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "1. Masukkan Data Akun",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (game.requiresServerId) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = userId,
                                    onValueChange = { userId = it },
                                    label = { Text("User ID") },
                                    placeholder = { Text(game.idPlaceholder) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1.3f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = serverId,
                                    onValueChange = { serverId = it },
                                    label = { Text("Zone / Server") },
                                    placeholder = { Text(game.serverPlaceholder) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        } else {
                            OutlinedTextField(
                                value = userId,
                                onValueChange = { userId = it },
                                label = { Text("User ID") },
                                placeholder = { Text(game.idPlaceholder) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = game.idHelperGuide,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp, color = TextMuted)
                            )
                        }
                    }
                }
            }

            // Step 2: Pilih Nominal Top Up
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "2. Pilih Nominal Top Up",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Grid of denominations
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            packages.chunked(2).forEach { rowPackages ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    rowPackages.forEach { pkg ->
                                        val isSelected = selectedPackage?.id == pkg.id
                                        Card(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { selectedPackage = pkg },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (isSelected) Color(0xFFFFFBEB) else Color.White
                                            ),
                                            border = BorderStroke(
                                                width = if (isSelected) 1.5.dp else 1.dp,
                                                color = if (isSelected) AkiviliPrimary else AkiviliOutline
                                            )
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text(
                                                    text = pkg.name,
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontSize = 13.sp,
                                                        color = if (isSelected) AkiviliPrimaryDark else TextPrimary
                                                    )
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = rupiahFormat.format(pkg.price),
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected) AkiviliPrimary else TextSecondary
                                                    )
                                                )
                                                if (pkg.bonus != null) {
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = pkg.bonus,
                                                        style = MaterialTheme.typography.labelMedium.copy(
                                                            color = StatusSuccess,
                                                            fontSize = 10.sp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    if (rowPackages.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Step 3: Kode Promo Voucher
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "3. Kode Promo (Opsional)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = promoCode,
                                onValueChange = { promoCode = it },
                                placeholder = { Text("Contoh: AKIVILIBARU") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Button(
                                onClick = { /* Apply Promo */ },
                                enabled = promoCode.isNotBlank(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AkiviliSecondary)
                            ) {
                                Text("Pakai")
                            }
                        }
                    }
                }
            }
        }
    }
}
