# ⚡ Akivili - Mobile Top-up Game Application (Android / Kotlin)

![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
![Language](https://img.shields.io/badge/Language-Kotlin%202.0-purple.svg)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-orange.svg)
![Status](https://img.shields.io/badge/Status-Design%20Analyzed%20%7C%20Ready%20for%20Implementation-brightgreen.svg)

**Akivili** adalah aplikasi mobile berbasis Android native (Kotlin) yang dirancang untuk memberikan pengalaman top-up game online yang cepat, aman, dan tanpa hambatan. Aplikasi ini mencakup katalog game populer, pemilihan nominal praktis, integrasi metode pembayaran lokal Indonesia (QRIS, E-Wallet, Virtual Account), serta pencatatan riwayat transaksi dan invoice digital.

---

## 🎨 Referensi Desain Figma
* **Figma URL**: [Akivili - APK Topup (Figma Design)](https://www.figma.com/design/VMvag1yybSv7ahGtPNeW1W/Akivili--APK-Topup-?node-id=0-1&m=dev&t=DhJmbvX0nuBMh757-1)
* **Desain Spesifikasi Lengkap**: Silakan baca dokumen resmi [PRD.md](file:///c:/Users/PIBI/OneDrive/Documents/Akivili/PRD.md).
* **Aset visual preview Figma**: Tersimpan di folder [`docs/figma-assets/`](file:///c:/Users/PIBI/OneDrive/Documents/Akivili/docs/figma-assets/).

---

## 🚀 Fitur Utama

### 1. Autentikasi & Akun
- **Splash Screen**: Layar pembuka bertema oranye khas Akivili.
- **Login & Register**: Pendaftaran dan masuk akun yang cepat dan aman.
- **Lupa Password**: Pemulihan akun melalui verifikasi kode OTP email.
- **Mode Tamu (Guest Mode)**: Fleksibilitas top-up instan tanpa harus mendaftar akun terlebih dahulu.

### 2. Beranda & Eksplorasi Game
- **Header Dinamis**: Menampilkan logo Akivili dan kolom pencarian game.
- **Banner Carousel Promosi**: Menampilkan event diskon dan cashback terkini.
- **Katalog Game (Grid 3-Kolom)**: Meliputi game populer seperti Mobile Legends, Honkai: Star Rail, Genshin Impact, Free Fire, Valorant, PUBG Mobile, Steam Wallet, Google Play Voucher, dll.

### 3. Formulir Pemesanan Top-up
- **Input Akun Fleksibel**: Mendukung input User ID, Zone ID, dan pemilihan Server secara dinamis sesuai game yang dipilih.
- **Pilihan Nominal Interaktif**: Grid kartu denominasi dengan harga real-time dan indikator seleksi aktif yang jelas.
- **Voucher Promo**: Dukungan kode promo untuk potongan harga.

### 4. Alur Pembayaran Lengkap
- **Seleksi Multi-Metode**:
  - **QRIS**: Scan & pay dengan GoPay, OVO, DANA, ShopeePay, BCA Mobile, Livin', dll.
  - **E-Wallet Direct**: Integrasi dompet digital.
  - **Virtual Account**: BCA, Mandiri, BRI, BNI.
- **Layar Tagihan / QRIS**: Menampilkan countdown timer 15 menit dan generator QR Code interaktif beserta tombol unduh QR.

### 5. Riwayat & Pusat Bantuan
- **Riwayat Transaksi**: Filter status transaksi (*Semua*, *Menunggu*, *Berhasil*, *Gagal*).
- **Invoice Digital**: Detail ringkasan pesanan yang dapat diverifikasi dan dibagikan.
- **Profil & Bantuan**: FAQ terintegrasi dan tombol kontak langsung CS via WhatsApp.

---

## 🛠️ Tech Stack & Library Rekomendasi

| Lapisan / Komponen | Teknologi yang Direkomendasikan | Alasan Pemilihan |
| :--- | :--- | :--- |
| **Language** | Kotlin 2.0+ | Bahasa standar resmi Google untuk Android modern |
| **UI Toolkit** | Jetpack Compose + Material 3 | Declarative UI, performa tinggi, dan mudah mengadaptasi desain Figma |
| **Arsitektur** | Clean Architecture + MVVM | Modularitas tinggi, mudah ditest (*unit testable*), dan *separation of concerns* |
| **Dependency Injection** | Hilt (Dagger) | Standar industri untuk Android DI |
| **Asynchronous** | Kotlin Coroutines & Flow | Penanganan operasi background dan state reaktif |
| **Networking** | Retrofit 2 + OkHttp 3 + Kotlinx Serialization | Koneksi REST API cepat dan tipe-aman |
| **Local Persistence** | Room Database & Jetpack DataStore | Caching transaksi lokal dan penyimpanan preferensi/token |
| **Image Loading** | Coil 3 (Compose Image) | Ringan, berbasis Coroutines, dan cepat |
| **QR Code** | ZXing Android / QRGEncoder | Render visual kode QRIS secara lokal |

---

## 📁 Struktur Direktori Proyek

Struktur paket Android yang direkomendasikan untuk pengembangan:

```
c:\Users\PIBI\OneDrive\Documents\Akivili\
 ├── docs/
 │    ├── figma-assets/        # Aset tangkapan layar & referensi Figma
 │    └── PRD.md               # Product Requirement Document (PRD)
 ├── README.md                 # Dokumentasi utama proyek
 └── app/ (Saat diinisialisasi di Android Studio)
      └── src/main/java/com/akivili/app/
           ├── core/
           │    ├── designsystem/   # Colors, Typography, Shapes, Icons
           │    ├── network/        # Retrofit Client, Interceptors
           │    └── util/           # Formatters (Rupiah, Date), Constants
           ├── data/
           │    ├── model/          # DTOs & Entity Database
           │    ├── repository/     # Implementasi Repository Data
           │    └── datasource/     # Remote & Local Data Sources
           ├── domain/
           │    ├── model/          # Pure Business Data Models
           │    ├── repository/     # Domain Repository Interfaces
           │    └── usecase/        # Use Cases (GetGames, CreateOrder, etc.)
           └── presentation/
                ├── auth/           # Login, Register, Forgot Password
                ├── home/           # Catalog, Banner Carousel, Search
                ├── topup/          # Form ID, Nominal Grid, Summary
                ├── payment/        # Payment Selection, QRIS Screen
                ├── history/        # Transaction History, Invoice Detail
                └── profile/        # User Profile, Help Center, Settings
```

---

## 🎯 Panduan Design Token & Warna

Berikut adalah token warna utama yang diambil langsung dari kanvas desain Figma:

| Token | Nilai Hex | Penggunaan di Aplikasi |
| :--- | :--- | :--- |
| `Primary` | `#FFA000` | Header TopBar, Tombol CTA Utama, Badge Aktif |
| `PrimaryVariant` | `#F59E0B` | Aksen gradasi, Status Pending |
| `Background` | `#FFFFFF` | Latar belakang halaman aplikasi |
| `SurfaceCard` | `#FFFDF7` / `#FFFFFF` | Latar kartu game & kartu nominal |
| `TextPrimary` | `#1E293B` | Judul, Label utama, Teks header |
| `TextSecondary` | `#64748B` | Subtitle, petunjuk input, teks keterangan |
| `StatusSuccess` | `#10B981` | Status transaksi "Berhasil" |
| `StatusPending` | `#F59E0B` | Status transaksi "Menunggu Pembayaran" |
| `StatusFailed` | `#EF4444` | Status transaksi "Gagal / Dibatalkan" |

---

## 🏁 Langkah Memulai Pengembangan (Quick Start)

### 1. Prasyarat
- **Android Studio Ladybug (2024.2+)** atau versi lebih baru.
- **JDK 17** atau **JDK 21**.
- **Android SDK API Level 34 / 35**.

### 2. Inisialisasi Proyek Baru di Android Studio
Jika Anda ingin membuat basis kode di folder ini:
1. Buka **Android Studio**.
2. Pilih **New Project** -> **Empty Activity (Compose)**.
3. Konfigurasikan:
   * **Name**: `Akivili`
   * **Package name**: `com.akivili.app`
   * **Save location**: `c:\Users\PIBI\OneDrive\Documents\Akivili`
   * **Language**: `Kotlin`
   * **Minimum SDK**: `API 24 ("Nougat"; Android 7.0)`
   * **Build configuration language**: `Kotlin DSL (build.gradle.kts)`
4. Klik **Finish** dan biarkan Gradle menyelesaikan proses indexing.

### 3. Membaca Dokumen Kebutuhan
Sebelum menulis logika bisnis, baca secara seksama dokumen **[PRD.md](file:///c:/Users/PIBI/OneDrive/Documents/Akivili/PRD.md)** yang berisi spesifikasi fungsional setiap tombol, validasi formulir, dan kontrak alur pemesanan.

---

## 📞 Kontribusi & Kontak
- **Desainer UI/UX**: Tim Desain Akivili
- **Lead Developer**: Mobile Engineering Team
- **Dokumentasi Terkait**: [PRD.md](file:///c:/Users/PIBI/OneDrive/Documents/Akivili/PRD.md)
