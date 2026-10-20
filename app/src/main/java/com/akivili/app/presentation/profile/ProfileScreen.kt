package com.akivili.app.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akivili.app.core.designsystem.animation.bouncyClick
import com.akivili.app.core.designsystem.theme.*
import com.akivili.app.domain.model.User
import com.akivili.app.presentation.components.AkiviliTopBar
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    user: User?,
    isGuest: Boolean,
    onUpdateProfile: (username: String, email: String, phone: String) -> Unit,
    onLogout: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }
    var showNotifSettingsDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AkiviliTopBar(title = "Profil & Pengaturan", showBackButton = false, showNotificationIcon = false)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AkiviliBackground)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Profile Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (user != null) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(AkiviliPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = AkiviliPrimaryDark,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = user.username,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = user.email,
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontSize = 12.sp)
                            )
                            Text(
                                text = user.phoneNumber,
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted, fontSize = 11.sp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                color = AkiviliPrimaryContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "⭐ ${user.points} Poin Loyalty",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = AkiviliPrimaryDark,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.5.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                } else {
                    // Guest state - clean & centered
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(AkiviliSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(42.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Mode Tamu (Guest)",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = TextPrimary
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Masuk untuk menikmati poin reward loyalti, riwayat pesanan lengkap, dan diskon eksklusif.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNavigateToLogin,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AkiviliPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Masuk / Daftar Akun",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }
            }

            // Menu Options Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    if (user != null) {
                        ProfileMenuItem(icon = Icons.Default.Edit, title = "Ubah Profil") {
                            showEditProfileDialog = true
                        }
                        HorizontalDivider(color = AkiviliOutline, modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileMenuItem(icon = Icons.Default.Security, title = "Keamanan & Kata Sandi") {
                            showSecurityDialog = true
                        }
                        HorizontalDivider(color = AkiviliOutline, modifier = Modifier.padding(horizontal = 16.dp))
                    }
                    ProfileMenuItem(icon = Icons.Default.Notifications, title = "Pengaturan Notifikasi") {
                        showNotifSettingsDialog = true
                    }
                    HorizontalDivider(color = AkiviliOutline, modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileMenuItem(icon = Icons.Default.Description, title = "Syarat & Ketentuan Layanan") {
                        showTermsDialog = true
                    }
                    HorizontalDivider(color = AkiviliOutline, modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileMenuItem(icon = Icons.Default.PrivacyTip, title = "Kebijakan Privasi") {
                        showPrivacyDialog = true
                    }
                }
            }

            // Logout Button
            if (user != null) {
                Button(
                    onClick = { showLogoutConfirmDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StatusFailedContainer)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = StatusFailed
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Keluar dari Akun",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = StatusFailed,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }

    // --- Interactive Dialogs ---

    // 1. Edit Profile Dialog
    if (showEditProfileDialog && user != null) {
        var editUsername by remember { mutableStateOf(user.username) }
        var editEmail by remember { mutableStateOf(user.email) }
        var editPhone by remember { mutableStateOf(user.phoneNumber) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Ubah Profil Saya", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editUsername,
                        onValueChange = { editUsername = it },
                        label = { Text("Username") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Nomor HP") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(editUsername, editEmail, editPhone)
                        showEditProfileDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Profil berhasil diperbarui.")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AkiviliPrimary)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // 2. Security Dialog
    if (showSecurityDialog) {
        var oldPass by remember { mutableStateOf("") }
        var newPass by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showSecurityDialog = false },
            title = { Text("Keamanan & Kata Sandi", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = oldPass,
                        onValueChange = { oldPass = it },
                        label = { Text("Kata Sandi Lama") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = { Text("Kata Sandi Baru") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSecurityDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Kata sandi berhasil diperbarui!")
                        }
                    },
                    enabled = oldPass.isNotBlank() && newPass.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = AkiviliPrimary)
                ) {
                    Text("Perbarui")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSecurityDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // 3. Notification Settings Dialog
    if (showNotifSettingsDialog) {
        var pushEnabled by remember { mutableStateOf(true) }
        var promoEnabled by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { showNotifSettingsDialog = false },
            title = { Text("Pengaturan Notifikasi", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Notifikasi Status Pesanan", fontSize = 13.sp)
                        Switch(checked = pushEnabled, onCheckedChange = { pushEnabled = it })
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Notifikasi Promo & Diskon", fontSize = 13.sp)
                        Switch(checked = promoEnabled, onCheckedChange = { promoEnabled = it })
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showNotifSettingsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AkiviliPrimary)
                ) {
                    Text("Tutup")
                }
            }
        )
    }

    // 4. Terms Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = { Text("Syarat & Ketentuan", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "1. Transaksi topup yang telah berhasil diproses ke User ID tujuan bersifat final dan tidak dapat dibatalkan.\n\n" +
                            "2. Pastikan memasukkan User ID dan Zone ID dengan benar sebelum melakukan pembayaran.\n\n" +
                            "3. Layanan beroperasi secara otomatis 24 jam setiap hari dengan estimasi pengiriman 1-5 menit.\n\n" +
                            "4. Jika terjadi kendala jaringan/maintenance dari publisher game, pengiriman akan diproses segera setelah server pulih.",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(onClick = { showTermsDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = AkiviliPrimary)) {
                    Text("Saya Mengerti")
                }
            }
        )
    }

    // 5. Privacy Policy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Kebijakan Privasi", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Akivili menghormati dan melindungi privasi seluruh pengguna. Informasi akun game (User ID) dan data kontak (Email / No. HP) hanya digunakan semata-mata untuk keperluan pemrosesan pesanan dan pengiriman bukti pembayaran digital. Kami tidak pernah membagikan data Anda kepada pihak ketiga yang tidak berwenang.",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(onClick = { showPrivacyDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = AkiviliPrimary)) {
                    Text("Tutup")
                }
            }
        )
    }

    // 6. Logout Confirmation Dialog
    if (showLogoutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirmDialog = false },
            title = { Text("Konfirmasi Keluar", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin keluar dari akun Akivili Anda?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirmDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusFailed)
                ) {
                    Text("Ya, Keluar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirmDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bouncyClick { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AkiviliPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(13.dp)
        )
    }
}
