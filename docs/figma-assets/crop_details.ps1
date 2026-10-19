Add-Type -AssemblyName System.Drawing
$filePath = Resolve-Path "figma_thumbnail.png"
$img = [System.Drawing.Bitmap]::FromFile($filePath)

function Crop($x, $y, $w, $h, $name) {
    $rect = New-Object System.Drawing.Rectangle($x, $y, $w, $h)
    $crop = $img.Clone($rect, $img.PixelFormat)
    $crop.Save("screen_$name.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $crop.Dispose()
    Write-Host "Saved screen_$name.png"
}

# Topup Screen 1 & 2
Crop 390 260 70 120 'topup_item1'
Crop 440 260 70 120 'topup_item2'
Crop 500 260 70 120 'topup_item3'

# Payment screen & QRIS
Crop 380 680 80 120 'payment_methods'
Crop 470 680 80 130 'qris_invoice'
Crop 590 680 80 120 'order_status'

# Auth screens
Crop 140 260 65 110 'auth_register'
Crop 205 260 65 110 'auth_login'

# Profile & Help
Crop 40 820 65 120 'profile_card'
Crop 240 820 70 120 'help_center'

$img.Dispose()
