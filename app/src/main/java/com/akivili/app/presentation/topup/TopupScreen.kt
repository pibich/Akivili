package com.akivili.app.presentation.topup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.akivili.app.core.designsystem.theme.*
import com.akivili.app.data.dummy.DummyData
import com.akivili.app.domain.model.Game
import com.akivili.app.domain.model.TopupPackage
import com.akivili.app.domain.model.Voucher
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

import com.akivili.app.core.designsystem.animation.bouncyClick
import com.akivili.app.presentation.components.AkiviliTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopupScreen(
    gameId: String,
    appliedVoucher: Voucher?,
    onApplyVoucher: (String) -> Pair<Boolean, String>,
    onRemoveVoucher: () -> Unit,
    onBackClick: () -> Unit,
    onProceedToPayment: (gameId: String, packageId: String, userId: String, serverId: String?) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val game = remember(gameId) {
        DummyData.games.find { it.id == gameId } ?: DummyData.games.first()
    }
    val packages = remember(gameId) {
        DummyData.getPackagesForGame(gameId)
    }

    var userId by remember { mutableStateOf("") }
    var serverId by remember { mutableStateOf(if (game.serverList.isNotEmpty()) game.serverList.first() else "") }
    var selectedPackage by remember { mutableStateOf<TopupPackage?>(packages.firstOrNull()) }
    var promoInput by remember { mutableStateOf("") }
    var serverDropdownExpanded by remember { mutableStateOf(false) }

    val rupiahFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
    }

    val isFormValid = userId.isNotBlank() && selectedPackage != null &&
            (!game.requiresServerId || serverId.isNotBlank())

    // Price calculations
    val originalPrice = selectedPackage?.price ?: 0L
    val discountAmount = if (appliedVoucher != null && selectedPackage != null) {
        if (appliedVoucher.discountPercent > 0) {
            (originalPrice * appliedVoucher.discountPercent) / 100
        } else {
            appliedVoucher.discountAmount
        }
    } else 0L

    val finalPrice = maxOf(0L, originalPrice - discountAmount)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AkiviliTopBar(
                title = game.title,
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
                            text = "Total Pembayaran",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontSize = 11.sp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = rupiahFormat.format(finalPrice),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AkiviliPrimaryDark,
                                    fontSize = 18.sp
                                )
                            )
                            if (discountAmount > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = rupiahFormat.format(originalPrice),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextMuted,
                                        fontSize = 11.sp,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                )
                            }
                        }
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
            // Header Game Card
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
                                .clip(RoundedCornerShape(12.dp))
                                .background(AkiviliSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = game.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = game.publisher,
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontSize = 12.sp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                color = AkiviliPrimaryContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "⚡ Pengiriman Otomatis 1-3 Menit",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = AkiviliPrimaryDark,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
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
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (game.requiresServerId && game.serverList.isNotEmpty()) {
                            // UID + Server Dropdown
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = userId,
                                    onValueChange = { userId = it },
                                    label = { Text("User ID") },
                                    placeholder = { Text(game.idPlaceholder, fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1.3f),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                ExposedDropdownMenuBox(
                                    expanded = serverDropdownExpanded,
                                    onExpandedChange = { serverDropdownExpanded = !serverDropdownExpanded },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    OutlinedTextField(
                                        value = serverId,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Server") },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serverDropdownExpanded) },
                                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    ExposedDropdownMenu(
                                        expanded = serverDropdownExpanded,
                                        onDismissRequest = { serverDropdownExpanded = false }
                                    ) {
                                        game.serverList.forEach { srv ->
                                            DropdownMenuItem(
                                                text = { Text(srv) },
                                                onClick = {
                                                    serverId = srv
                                                    serverDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        } else if (game.requiresServerId) {
                            // UID + Zone ID (e.g. MLBB)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = userId,
                                    onValueChange = { userId = it },
                                    label = { Text("User ID") },
                                    placeholder = { Text(game.idPlaceholder, fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1.3f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = serverId,
                                    onValueChange = { serverId = it },
                                    label = { Text("Zone ID") },
                                    placeholder = { Text(game.serverPlaceholder, fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        } else {
                            // Single UID
                            OutlinedTextField(
                                value = userId,
                                onValueChange = { userId = it },
                                label = { Text("User ID / Nomor Akun") },
                                placeholder = { Text(game.idPlaceholder, fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = TextMuted, modifier = Modifier.size(15.dp))
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
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            packages.chunked(2).forEach { rowPackages ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    rowPackages.forEach { pkg ->
                                        val isSelected = selectedPackage?.id == pkg.id
                                        val animatedBorderColor by androidx.compose.animation.animateColorAsState(
                                            targetValue = if (isSelected) AkiviliPrimary else AkiviliOutline,
                                            animationSpec = androidx.compose.animation.core.tween(200),
                                            label = "BorderColor"
                                        )
                                        val animatedBgColor by androidx.compose.animation.animateColorAsState(
                                            targetValue = if (isSelected) Color(0xFFFFFBEB) else Color.White,
                                            animationSpec = androidx.compose.animation.core.tween(200),
                                            label = "BgColor"
                                        )

                                        Card(
                                            modifier = Modifier
                                                .weight(1f)
                                                .bouncyClick { selectedPackage = pkg },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = animatedBgColor
                                            ),
                                            border = BorderStroke(
                                                width = if (isSelected) 1.8.dp else 1.dp,
                                                color = animatedBorderColor
                                            )
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.Top
                                                ) {
                                                    Text(
                                                        text = pkg.name,
                                                        style = MaterialTheme.typography.titleMedium.copy(
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 13.sp,
                                                            color = if (isSelected) AkiviliPrimaryDark else TextPrimary
                                                        ),
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                    if (isSelected) {
                                                        Icon(
                                                            imageVector = Icons.Default.CheckCircle,
                                                            contentDescription = "Terpilih",
                                                            tint = AkiviliPrimary,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = rupiahFormat.format(pkg.price),
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 12.sp,
                                                        color = if (isSelected) AkiviliPrimary else TextSecondary
                                                    )
                                                )
                                                if (pkg.bonus != null) {
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Surface(
                                                        color = StatusSuccessContainer,
                                                        shape = RoundedCornerShape(4.dp)
                                                    ) {
                                                        Text(
                                                            text = pkg.bonus,
                                                            style = MaterialTheme.typography.labelMedium.copy(
                                                                color = StatusSuccess,
                                                                fontSize = 9.5.sp,
                                                                fontWeight = FontWeight.Bold
                                                            ),
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                        )
                                                    }
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

            // Step 3: Kode Promo & Voucher
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = AkiviliPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "3. Kode Promo & Voucher (Opsional)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (appliedVoucher != null) {
                            // Applied voucher card
                            Surface(
                                color = StatusSuccessContainer,
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, StatusSuccess.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Voucher: ${appliedVoucher.code}",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = StatusSuccess,
                                                fontSize = 13.sp
                                            )
                                        )
                                        Text(
                                            text = appliedVoucher.description,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = TextSecondary,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }

                                    IconButton(onClick = onRemoveVoucher) {
                                        Icon(Icons.Default.Close, contentDescription = "Hapus Voucher", tint = StatusFailed)
                                    }
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = promoInput,
                                    onValueChange = { promoInput = it.uppercase() },
                                    placeholder = { Text("Contoh: AKIVILI10", fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                Button(
                                    onClick = {
                                        val (success, msg) = onApplyVoucher(promoInput)
                                        scope.launch {
                                            snackbarHostState.showSnackbar(msg)
                                        }
                                        if (success) {
                                            promoInput = ""
                                        }
                                    },
                                    enabled = promoInput.isNotBlank(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = AkiviliSecondary)
                                ) {
                                    Text("Pakai")
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quick voucher suggestions
                            Text(
                                text = "Voucher Tersedia:",
                                style = MaterialTheme.typography.labelMedium.copy(color = TextMuted, fontSize = 11.sp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("AKIVILI10", "HEMAT5K", "SULTANPASS").forEach { code ->
                                    SuggestionChip(
                                        onClick = {
                                            promoInput = code
                                            val (success, msg) = onApplyVoucher(code)
                                            scope.launch {
                                                snackbarHostState.showSnackbar(msg)
                                            }
                                        },
                                        label = { Text(code, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
