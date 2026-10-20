package com.akivili.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.akivili.app.data.dummy.DummyData
import com.akivili.app.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class AkiviliViewModel : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(DummyData.sampleUser)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isGuest = MutableStateFlow(false)
    val isGuest: StateFlow<Boolean> = _isGuest.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(DummyData.sampleTransactions)
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(DummyData.sampleNotifications)
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _appliedVoucher = MutableStateFlow<Voucher?>(null)
    val appliedVoucher: StateFlow<Voucher?> = _appliedVoucher.asStateFlow()

    // --- Authentication Actions ---
    fun login(emailOrUsername: String, pass: String): Boolean {
        if (emailOrUsername.isBlank() || pass.isBlank()) return false
        _isGuest.value = false
        _currentUser.value = DummyData.sampleUser.copy(
            username = if (emailOrUsername.contains("@")) emailOrUsername.substringBefore("@") else emailOrUsername,
            email = if (emailOrUsername.contains("@")) emailOrUsername else "user@akivili.id"
        )
        return true
    }

    fun continueAsGuest() {
        _isGuest.value = true
        _currentUser.value = null
    }

    fun register(username: String, email: String, phone: String, pass: String): Boolean {
        if (username.isBlank() || email.isBlank() || pass.isBlank()) return false
        _isGuest.value = false
        _currentUser.value = User(
            id = "usr_${System.currentTimeMillis().toString().takeLast(6)}",
            username = username,
            email = email,
            phoneNumber = phone,
            points = 500
        )
        return true
    }

    fun logout() {
        _currentUser.value = null
        _isGuest.value = false
    }

    fun updateProfile(username: String, email: String, phone: String) {
        val current = _currentUser.value ?: return
        _currentUser.value = current.copy(
            username = username,
            email = email,
            phoneNumber = phone
        )
    }

    // --- Voucher Actions ---
    fun applyVoucher(code: String): Pair<Boolean, String> {
        val cleanCode = code.trim().uppercase()
        val found = DummyData.vouchers.find { it.code.equals(cleanCode, ignoreCase = true) }
        return if (found != null) {
            _appliedVoucher.value = found
            Pair(true, "Voucher ${found.code} berhasil dipasang: ${found.description}")
        } else {
            Pair(false, "Kode voucher '$code' tidak valid atau telah kedaluwarsa.")
        }
    }

    fun removeVoucher() {
        _appliedVoucher.value = null
    }

    // --- Transaction Actions ---
    fun createTransaction(
        gameId: String,
        packageId: String,
        userId: String,
        serverId: String?,
        paymentMethodCode: String
    ): Transaction {
        val game = DummyData.games.find { it.id == gameId } ?: DummyData.games.first()
        val pkg = DummyData.getPackagesForGame(gameId).find { it.id == packageId }
            ?: DummyData.getPackagesForGame(gameId).first()
        val method = DummyData.paymentMethods.find { it.code == paymentMethodCode }
            ?: DummyData.paymentMethods.first()

        val subtotal = pkg.price
        val voucher = _appliedVoucher.value
        val discount = if (voucher != null) {
            if (voucher.discountPercent > 0) (subtotal * voucher.discountPercent) / 100
            else voucher.discountAmount
        } else 0L

        val discountedSubtotal = maxOf(0L, subtotal - discount)
        val total = discountedSubtotal + method.adminFee

        val newInvoiceId = "INV-AKV-${System.currentTimeMillis().toString().takeLast(8)}"
        val transaction = Transaction(
            invoiceId = newInvoiceId,
            gameId = game.id,
            gameTitle = game.title,
            packageName = pkg.name,
            userId = userId,
            serverId = if (serverId == "NONE") null else serverId,
            paymentMethodName = method.name,
            paymentMethodCode = method.code,
            subtotal = subtotal,
            adminFee = method.adminFee,
            totalAmount = total,
            status = TransactionStatus.PENDING,
            createdAt = System.currentTimeMillis(),
            expiresAt = System.currentTimeMillis() + (15 * 60 * 1000L),
            qrisPayload = "00020101021226670016ID.CO.AKIVILI.WWW01189360099900000000015204581253033605802ID5914AKIVILI TOPUP6007JAKARTA"
        )

        _transactions.value = listOf(transaction) + _transactions.value

        // Award loyalty points to user
        _currentUser.value?.let { user ->
            _currentUser.value = user.copy(points = user.points + (total / 1000).toInt())
        }

        // Add notification
        val newNotif = AppNotification(
            id = "notif_${System.currentTimeMillis()}",
            title = "Tagihan Baru Dibuat",
            message = "Menunggu pembayaran untuk ${game.title} - ${pkg.name} ($newInvoiceId)",
            timeAgo = "Baru saja",
            isRead = false
        )
        _notifications.value = listOf(newNotif) + _notifications.value

        return transaction
    }

    fun getTransaction(invoiceId: String): Transaction? {
        return _transactions.value.find { it.invoiceId == invoiceId }
    }

    fun markTransactionSuccess(invoiceId: String) {
        _transactions.value = _transactions.value.map { trx ->
            if (trx.invoiceId == invoiceId) {
                trx.copy(status = TransactionStatus.SUCCESS)
            } else trx
        }

        val completedTrx = _transactions.value.find { it.invoiceId == invoiceId }
        if (completedTrx != null) {
            val successNotif = AppNotification(
                id = "notif_succ_${System.currentTimeMillis()}",
                title = "Top-up Berhasil!",
                message = "Top-up ${completedTrx.packageName} ${completedTrx.gameTitle} (${completedTrx.invoiceId}) telah sukses dikirim ke akun ${completedTrx.userId}.",
                timeAgo = "Baru saja",
                isRead = false
            )
            _notifications.value = listOf(successNotif) + _notifications.value
        }
    }

    // --- Notification Actions ---
    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }
}
