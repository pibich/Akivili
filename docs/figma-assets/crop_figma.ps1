Add-Type -AssemblyName System.Drawing
$filePath = Resolve-Path "figma_thumbnail.png"
$img = [System.Drawing.Bitmap]::FromFile($filePath)

function Crop($x, $y, $w, $h, $name) {
    $rect = New-Object System.Drawing.Rectangle($x, $y, $w, $h)
    $crop = $img.Clone($rect, $img.PixelFormat)
    $crop.Save("crop_$name.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $crop.Dispose()
    Write-Host "Saved crop_$name.png"
}

# 1. Auth & Splash (left side)
Crop 0 240 380 260 'auth'
# 2. Home screen & games
Crop 240 240 140 180 'home'
# 3. Game Topup Details (columns)
Crop 390 240 350 260 'topup_details'
Crop 740 240 370 260 'topup_details_2'
# 4. Checkout, Payment, QRIS & Invoice
Crop 40 660 670 160 'payment_flow'
# 5. Profile & History
Crop 40 810 380 180 'profile_history'
# 6. Forms & Settings (Bottom)
Crop 40 990 460 200 'settings_forms'
# 7. Top Banner / Header
Crop 420 50 230 140 'top_assets'

$img.Dispose()
