# ShowedUp

**Tamper-proof student attendance tracking for Android.**

ShowedUp automatically records your class attendance using multi-modal device signals (GPS, WiFi, Bluetooth, audio fingerprint, sensor data), cryptographically signs every record with ECDSA, and chains them with SHA-256 hashes — making retroactive tampering detectable.

---

## Features

| Feature | Description |
|---------|-------------|
| **Multi-Modal Signals** | 5 concurrent collectors (GPS, WiFi, BT, audio, sensor) with 30s timeout |
| **Tamper-Proof Records** | ECDSA P-256 signatures + SHA-256 hash chains |
| **AES-256-GCM Encryption** | All logs encrypted at rest with PBKDF2-derived keys |
| **Biometric Lock** | BiometricPrompt on every app open, 5-failure lockout (30 min) |
| **Device Integrity** | Root, emulator, and APK tamper detection on launch |
| **Schedule Management** | Add classes, set active days, day-off tracking |
| **PDF Export** | Generate and share attendance reports |
| **Privacy First** | No servers, no accounts — all data stays on-device |

## Screens

1. **Onboarding** — 3-step animated intro with permissions setup
2. **Home** — Today's classes, attendance progress, quick actions
3. **Calendar** — Month grid with attendance indicators and date detail
4. **Schedule Configurator** — Day toggles, add/edit/delete classes
5. **Day-Off Planner** — Sick/Personal/Holiday/Event with confirmation
6. **Attendance Log** — Searchable, filterable, expandable records with signal dots
7. **Export PDF** — Date range selection, PDF generation, share via intent
8. **Signal Overlay** — Floating pill during attendance collection

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **DI**: Hilt
- **Database**: Room
- **Background**: WorkManager
- **Crypto**: Android Keystore (ECDSA P-256), AES-256-GCM, SHA-256
- **Auth**: BiometricPrompt
- **Min SDK**: 31 (Android 12)
- **Target SDK**: 35

## Project Structure

```
app/src/main/java/com/showedup/app/
├── crypto/           # KeystoreManager, AesGcmCipher, HashUtils, CanonicalJson
├── data/
│   ├── dao/          # Room DAOs
│   ├── entity/       # Room entities
│   └── repository/   # Attendance, Schedule, Security repositories
├── di/               # Hilt AppModule
├── scheduler/        # AttendanceTriggerWorker, BootReceiver
├── security/         # BiometricAuthManager, DeviceIntegrityChecker
├── signal/           # MultiModalSignalCollector, SignalCollectionService
└── ui/
    ├── components/   # StatusBadge, SignalRow, PillChip, Buttons, etc.
    ├── navigation/   # NavHost + Screen routes
    ├── screens/      # All 8 screens with ViewModels
    └── theme/        # Color, Type, Shape, Spacing, Theme
```
## Building

### Prerequisites

- **Android Studio** Iguana or later
- **JDK 17**
- **Android SDK** with platform API 35 and build-tools

### Debug APK

```bash
./gradlew assembleDebug
```

The APK will be at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Release APK (signed)

1. Generate a keystore (one-time):
   ```bash
   keytool -genkey -v -keystore showedup-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias showedup
   ```

2. Create `keystore.properties` in the project root:
   ```properties
   storeFile=../showedup-release.jks
   storePassword=your_store_password
   keyAlias=showedup
   keyPassword=your_key_password
   ```

3. Build the release APK:
   ```bash
   ./gradlew assembleRelease
   ```

   Output: `app/build/outputs/apk/release/app-release.apk`

## Security

- `FLAG_SECURE` on all windows — screenshots blocked
- `android:allowBackup="false"` — no cloud/USB backup
- No cleartext traffic (`network_security_config.xml`)
- Internal file permissions set to `0600`
- Background location explicitly removed from manifest
- R8 full-mode obfuscation in release builds

## Future
- Easier class schedule generation
- attendance percentage calculator

## License

This project is private and not licensed for modification or distribution use.
