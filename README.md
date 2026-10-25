# NextBot.apk (launcher NEXUS BOT)

APK ini membuka bot yang jalan di Termux: sekali tap -> bot dinyalakan otomatis -> panel tampil di dalam app.
Folder bot harus ada di `~/NexusBot` (Termux).

## Build lewat GitHub Actions
1. Upload isi folder ini ke repo GitHub (harus ada `.github/workflows/build.yml`).
2. Tab Actions -> "Build NextBot.apk" -> Run workflow (atau otomatis tiap push).
3. Selesai -> buka run-nya -> Artifacts -> `NextBot-apk` -> download -> unzip -> `NextBot.apk`.

## Setup sekali di HP
1. Termux dari F-Droid, dan bot sudah ada di `~/NexusBot` (sudah pernah `bash install-termux.sh`).
2. Di Termux:
   ```
   mkdir -p ~/.termux
   echo "allow-external-apps=true" >> ~/.termux/termux.properties
   termux-reload-settings
   ```
3. Install NextBot.apk, buka, izinkan "Run commands in Termux environment".
4. Matikan pembatasan baterai untuk Termux (dan NextBot) biar bot tidak dibunuh sistem.
