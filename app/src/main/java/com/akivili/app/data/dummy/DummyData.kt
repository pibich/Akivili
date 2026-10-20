package com.akivili.app.data.dummy

import com.akivili.app.domain.model.*

object DummyData {

    val promoBanners = listOf(
        "https://cdn1.codashop.com/S/content/common/images/promos/Oct26/ID_MLBB_Starlight_Member_Hayabusa_Kitsune_Shadow_730x280.png",
        "https://cdn1.codashop.com/S/content/common/images/promos/Oct26/FF-Cotton%20Teddy%20Bundle-730x280.png",
        "https://cdn1.codashop.com/S/content/common/images/promos/Oct26/ID-Valorant-Champions2026XShopeepay-730x280.png"
    )

    val games = listOf(
        Game(
            id = "mlbb",
            title = "Mobile Legends",
            publisher = "Moonton",
            category = "Mobile",
            imageUrl = "https://cdn1.codashop.com/S/content/mobile/images/product-tiles/MLBB-2025-tiles-178x178.jpg",
            bannerUrl = "https://cdn1.codashop.com/S/content/common/images/promos/Oct26/ID_MLBB_Starlight_Member_Hayabusa_Kitsune_Shadow_730x280.png",
            requiresServerId = true,
            idPlaceholder = "Contoh: 12345678",
            serverPlaceholder = "Contoh: (2134)",
            idHelperGuide = "User ID dan Zone ID dapat ditemukan dengan klik avatar profil di pojok kiri atas menu game."
        ),
        Game(
            id = "hsr",
            title = "Honkai: Star Rail",
            publisher = "HoYoverse",
            category = "RPG",
            imageUrl = "https://cdn1.codashop.com/S/content/mobile/images/product-tiles/hsr_tile.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1579373903781-fd5c0c30c4cd?w=800&q=80",
            requiresServerId = true,
            serverList = listOf("Asia", "America", "Europe", "TW/HK/MO"),
            idPlaceholder = "Contoh: 800123456",
            serverPlaceholder = "Pilih Server",
            idHelperGuide = "UID berada di pojok kanan bawah layar saat dalam game atau pada menu Phone."
        ),
        Game(
            id = "genshin",
            title = "Genshin Impact",
            publisher = "HoYoverse",
            category = "RPG",
            imageUrl = "https://cdn1.codashop.com/S/content/mobile/images/product-tiles/Genshin_Impact_6th_Anniversary_Tile_178x178.png",
            bannerUrl = "https://images.unsplash.com/photo-1534423861386-85a16f5d13fd?w=800&q=80",
            requiresServerId = true,
            serverList = listOf("Asia", "America", "Europe", "TW/HK/MO"),
            idPlaceholder = "Contoh: 812345678",
            serverPlaceholder = "Pilih Server",
            idHelperGuide = "UID tertera pada menu Paimon di bawah avatar karakter."
        ),
        Game(
            id = "freefire",
            title = "Free Fire",
            publisher = "Garena",
            category = "Mobile",
            imageUrl = "https://cdn1.codashop.com/S/content/mobile/images/product-tiles/Free_Fire_Lapak_178x178.png",
            bannerUrl = "https://cdn1.codashop.com/S/content/common/images/promos/Oct26/FF-Cotton%20Teddy%20Bundle-730x280.png",
            requiresServerId = false,
            idPlaceholder = "Contoh: 1234567890",
            idHelperGuide = "Buka menu profil akun Free Fire Anda untuk menyalin nomor Player ID."
        ),
        Game(
            id = "valorant",
            title = "Valorant",
            publisher = "Riot Games",
            category = "PC",
            imageUrl = "https://cdn1.codashop.com/S/content/mobile/images/product-tiles/valorant_tile.jpg",
            bannerUrl = "https://cdn1.codashop.com/S/content/common/images/promos/Oct26/ID-Valorant-Champions2026XShopeepay-730x280.png",
            requiresServerId = false,
            idPlaceholder = "Contoh: Player#ID1",
            idHelperGuide = "Masukkan Riot ID lengkap beserta hashtag, misal: Akivili#IDN."
        ),
        Game(
            id = "pubgm",
            title = "PUBG Mobile",
            publisher = "Level Infinite",
            category = "Mobile",
            imageUrl = "https://cdn1.codashop.com/S/content/mobile/images/product-tiles/pubgm_tile_aug2024.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1542751110-97427bbecf20?w=800&q=80",
            requiresServerId = false,
            idPlaceholder = "Contoh: 5123456789",
            idHelperGuide = "Nomor Player ID dapat dilihat pada bagian atas profil karakter."
        ),
        Game(
            id = "steam",
            title = "Steam Wallet IDR",
            publisher = "Valve",
            category = "Voucher",
            imageUrl = "https://cdn1.codashop.com/S/content/mobile/images/product-tiles/steam_tile.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1612287271131-0b5c1c8f15d7?w=800&q=80",
            requiresServerId = false,
            idPlaceholder = "Masukkan Nomor WhatsApp / Email",
            idHelperGuide = "Kode voucher Steam akan dikirimkan langsung melalui WhatsApp & Email."
        ),
        Game(
            id = "googleplay",
            title = "Google Play",
            publisher = "Google LLC",
            category = "Voucher",
            imageUrl = "https://cdn1.codashop.com/S/content/mobile/images/product-tiles/googleplay_tile.png",
            bannerUrl = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=800&q=80",
            requiresServerId = false,
            idPlaceholder = "Masukkan Nomor WhatsApp / Email",
            idHelperGuide = "Kode voucher Google Play akan dikirimkan otomatis setelah bayar."
        ),
        Game(
            id = "codm",
            title = "Call of Duty: Mobile",
            publisher = "Garena",
            category = "Mobile",
            imageUrl = "https://cdn1.codashop.com/S/content/mobile/images/product-tiles/codmobile_tile.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&q=80",
            requiresServerId = false,
            idPlaceholder = "Contoh: 67489201948",
            idHelperGuide = "OpenID dapat disalin dari menu Pengaturan -> Lainnya di dalam game."
        ),
        Game(
            id = "roblox",
            title = "Roblox",
            publisher = "Roblox Corporation",
            category = "Voucher",
            imageUrl = "https://cdn1.codashop.com/S/content/mobile/images/product-tiles/Roblox-Gift-Cards-tile.png",
            bannerUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&q=80",
            requiresServerId = false,
            idPlaceholder = "Username Akun Roblox",
            idHelperGuide = "Pastikan Username Roblox yang dimasukkan sudah sesuai."
        ),
        Game(
            id = "zzz",
            title = "Zenless Zone Zero",
            publisher = "HoYoverse",
            category = "RPG",
            imageUrl = "https://cdn1.codashop.com/S/content/mobile/images/product-tiles/ZZZ_Zenless-Zone-Zero-Tile.png",
            bannerUrl = "https://images.unsplash.com/photo-1538481199705-c710c4e965fc?w=800&q=80",
            requiresServerId = true,
            serverList = listOf("Asia", "America", "Europe", "TW/HK/MO"),
            idPlaceholder = "Contoh: 100123456",
            serverPlaceholder = "Pilih Server",
            idHelperGuide = "UID tertera pada menu Profil di kiri bawah layar."
        )
    )

    fun getPackagesForGame(gameId: String): List<TopupPackage> = when (gameId) {
        "mlbb" -> listOf(
            TopupPackage("ml-1", "mlbb", "86 Diamonds", 20000),
            TopupPackage("ml-2", "mlbb", "172 Diamonds", 40000),
            TopupPackage("ml-3", "mlbb", "257 Diamonds", 60000, bonus = "+26 Bonus", isPopular = true),
            TopupPackage("ml-4", "mlbb", "344 Diamonds", 80000),
            TopupPackage("ml-5", "mlbb", "429 Diamonds", 100000),
            TopupPackage("ml-6", "mlbb", "514 Diamonds", 120000),
            TopupPackage("ml-7", "mlbb", "706 Diamonds", 160000, isPopular = true),
            TopupPackage("ml-8", "mlbb", "Twilight Pass", 145000)
        )
        "hsr" -> listOf(
            TopupPackage("hsr-1", "hsr", "60 Oneiric Shards", 16000),
            TopupPackage("hsr-2", "hsr", "300+30 Oneiric", 79000),
            TopupPackage("hsr-3", "hsr", "Express Supply Pass", 79000, bonus = "30 Hari", isPopular = true),
            TopupPackage("hsr-4", "hsr", "980+110 Oneiric", 249000),
            TopupPackage("hsr-5", "hsr", "1980+260 Oneiric", 479000),
            TopupPackage("hsr-6", "hsr", "3280+600 Oneiric", 799000)
        )
        "genshin" -> listOf(
            TopupPackage("gi-1", "genshin", "60 Genesis Crystals", 16000),
            TopupPackage("gi-2", "genshin", "Blessing of Welkin Moon", 79000, bonus = "30 Hari", isPopular = true),
            TopupPackage("gi-3", "genshin", "300+30 Genesis Crystals", 79000),
            TopupPackage("gi-4", "genshin", "980+110 Genesis Crystals", 249000),
            TopupPackage("gi-5", "genshin", "1980+260 Genesis Crystals", 479000)
        )
        "freefire" -> listOf(
            TopupPackage("ff-1", "freefire", "70 Diamonds", 10000),
            TopupPackage("ff-2", "freefire", "140 Diamonds", 20000),
            TopupPackage("ff-3", "freefire", "355 Diamonds", 50000, isPopular = true),
            TopupPackage("ff-4", "freefire", "720 Diamonds", 100000),
            TopupPackage("ff-5", "freefire", "1450 Diamonds", 200000)
        )
        "steam" -> listOf(
            TopupPackage("st-1", "steam", "Steam Wallet IDR 12.000", 14000),
            TopupPackage("st-2", "steam", "Steam Wallet IDR 45.000", 50000),
            TopupPackage("st-3", "steam", "Steam Wallet IDR 90.000", 98000, isPopular = true),
            TopupPackage("st-4", "steam", "Steam Wallet IDR 250.000", 265000),
            TopupPackage("st-5", "steam", "Steam Wallet IDR 400.000", 420000)
        )
        else -> listOf(
            TopupPackage("def-1", gameId, "Paket Pemula", 25000),
            TopupPackage("def-2", gameId, "Paket Populer", 75000, isPopular = true),
            TopupPackage("def-3", gameId, "Paket Sultan", 150000)
        )
    }

    val paymentMethods = listOf(
        PaymentMethod(
            code = "QRIS",
            name = "QRIS (Semua E-Wallet & Mobile Banking)",
            category = PaymentCategory.QRIS,
            adminFee = 800,
            instructions = listOf(
                "Buka aplikasi mobile banking atau e-wallet Anda.",
                "Pilih menu 'Scan' atau 'Bayar dengan QRIS'.",
                "Scan kode QR yang tampil di layar.",
                "Periksa nominal dan selesaikan transaksi."
            )
        ),
        PaymentMethod(
            code = "GOPAY",
            name = "GoPay",
            category = PaymentCategory.E_WALLET,
            adminFee = 1000
        ),
        PaymentMethod(
            code = "DANA",
            name = "DANA",
            category = PaymentCategory.E_WALLET,
            adminFee = 1000
        ),
        PaymentMethod(
            code = "OVO",
            name = "OVO",
            category = PaymentCategory.E_WALLET,
            adminFee = 1000
        ),
        PaymentMethod(
            code = "SHOPEEPAY",
            name = "ShopeePay",
            category = PaymentCategory.E_WALLET,
            adminFee = 1000
        ),
        PaymentMethod(
            code = "BCA_VA",
            name = "BCA Virtual Account",
            category = PaymentCategory.VIRTUAL_ACCOUNT,
            adminFee = 2500
        ),
        PaymentMethod(
            code = "MANDIRI_VA",
            name = "Mandiri Virtual Account",
            category = PaymentCategory.VIRTUAL_ACCOUNT,
            adminFee = 2500
        ),
        PaymentMethod(
            code = "BRI_VA",
            name = "BRI Virtual Account",
            category = PaymentCategory.VIRTUAL_ACCOUNT,
            adminFee = 2500
        )
    )

    val sampleUser = User(
        id = "usr_01",
        username = "Trailblazer",
        email = "user@akivili.id",
        phoneNumber = "+62 812-3456-7890",
        points = 1250
    )

    val sampleTransactions = listOf(
        Transaction(
            invoiceId = "INV-AKV-20261020-001",
            gameId = "mlbb",
            gameTitle = "Mobile Legends",
            packageName = "257 Diamonds",
            userId = "12345678",
            serverId = "2134",
            paymentMethodName = "QRIS",
            paymentMethodCode = "QRIS",
            subtotal = 60000,
            adminFee = 800,
            totalAmount = 60800,
            status = TransactionStatus.SUCCESS,
            createdAt = System.currentTimeMillis() - 86400000L,
            expiresAt = System.currentTimeMillis() - 85500000L
        ),
        Transaction(
            invoiceId = "INV-AKV-20261020-002",
            gameId = "hsr",
            gameTitle = "Honkai: Star Rail",
            packageName = "Express Supply Pass",
            userId = "800123456",
            serverId = "Asia",
            paymentMethodName = "BCA Virtual Account",
            paymentMethodCode = "BCA_VA",
            subtotal = 79000,
            adminFee = 2500,
            totalAmount = 81500,
            status = TransactionStatus.PENDING,
            createdAt = System.currentTimeMillis() - 600000L,
            expiresAt = System.currentTimeMillis() + 900000L
        ),
        Transaction(
            invoiceId = "INV-AKV-20261019-098",
            gameId = "freefire",
            gameTitle = "Free Fire",
            packageName = "720 Diamonds",
            userId = "9876543210",
            paymentMethodName = "DANA",
            paymentMethodCode = "DANA",
            subtotal = 100000,
            adminFee = 1000,
            totalAmount = 101000,
            status = TransactionStatus.FAILED,
            createdAt = System.currentTimeMillis() - 172800000L,
            expiresAt = System.currentTimeMillis() - 171900000L
        )
    )

    val vouchers = listOf(
        Voucher("AKIVILI10", discountPercent = 10, description = "Diskon 10% Pengguna Baru"),
        Voucher("HEMAT5K", discountAmount = 5000, description = "Potongan Langsung Rp 5.000"),
        Voucher("SULTANPASS", discountPercent = 15, description = "Diskon Khusus 15%"),
        Voucher("GACHAAMAN", discountAmount = 10000, description = "Potongan Spesial Rp 10.000")
    )

    val sampleNotifications = listOf(
        AppNotification("notif_1", "Selamat Datang di Akivili!", "Nikmati kemudahan top-up game instan & terpercaya dengan berbagai promo menarik.", "Baru saja", false),
        AppNotification("notif_2", "Promo Spesial QRIS 0% Fee", "Gunakan pembayaran QRIS bebas biaya admin hingga akhir pekan!", "2 jam lalu", false),
        AppNotification("notif_3", "Pesanan Berhasil", "Top-up 257 Diamonds Mobile Legends (#INV-AKV-20261020-001) telah berhasil dikirim ke akun Anda.", "1 hari lalu", true)
    )
}
