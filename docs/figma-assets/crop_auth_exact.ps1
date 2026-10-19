Add-Type -AssemblyName System.Drawing
$filePath = Resolve-Path "crop_auth.png"
$img = [System.Drawing.Bitmap]::FromFile($filePath)

function Crop($x, $y, $w, $h, $name) {
    $rect = New-Object System.Drawing.Rectangle($x, $y, $w, $h)
    $crop = $img.Clone($rect, $img.PixelFormat)
    $crop.Save("exact_$name.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $crop.Dispose()
    Write-Host "Saved exact_$name.png"
}

Crop 0 20 50 110 'splash'
Crop 50 20 60 110 'register'
Crop 110 20 60 110 'login'
Crop 170 20 60 110 'forgot_password'
Crop 260 20 55 120 'home'

$img.Dispose()
