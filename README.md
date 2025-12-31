# 🛠️ Swiss Army - Your Digital Utility Belt

A comprehensive Android utility application built with modern architecture patterns, Jetpack Compose, and Material 3 design.

![Android](https://img.shields.io/badge/Android-SDK%2036-green?logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-blue)
![Architecture](https://img.shields.io/badge/Architecture-Multi--Module-orange)

---

## 📱 Features

### 📄 Document Scanner
- High-quality document scanning using Google ML Kit
- Automatic edge detection and perspective correction
- Export as PDF (auto-compressed under 800KB)
- Open, Share, Rename, and New Scan actions

### 📕 PDF Tools
- **Compress PDF**: Reduce file size with adjustable quality
- **Remove Password**: Unlock protected PDFs
- Open, Share, Rename processed files
- Uses Apache PDFBox for processing

### 🖼️ Image Compressor
- JPEG compression with quality slider (10-100%)
- Preview before and after compression
- Open, Share, Rename, Close actions
- Uses Android Bitmap compression

### 🧮 Financial Calculators
- **SIP Calculator**: Systematic Investment Plan returns
- **EMI Calculator**: Loan EMI with amortization
- **Tax Calculator**: Income tax estimation (Old & New regime)

### 🧹 RCS Message Cleaner
- Scan inbox for OTPs, verification codes, and promotional messages
- Bulk delete all junk messages in one go
- Identifies business senders (shortcodes, alphanumeric IDs)
- Requires Default SMS App permission for deletion

### 🌍 World Clock
- Display time across multiple time zones
- Clean, modern UI with live updates
- Popular cities pre-configured

### 📖 Dictionary
- Word definitions and meanings
- Clean lookup interface

### 📊 ASCII Table
- Complete ASCII reference chart
- Decimal, Hex, and Character display

### ⚙️ Settings
- **Theme**: Dark Mode, Light Mode, System Default
- **Default Scan Quality**: 50-100%
- **Default Compression Level**: 10-90%
- **Clear Cache**: Free up storage space

---

## 🏗️ Architecture

### Multi-Module Structure

```
SwissArmy/
├── app/                          # Main application module
├── core/
│   ├── core-ui/                  # Theme, components, design system
│   ├── core-common/              # Shared utilities
│   └── core-navigation/          # Navigation abstractions
└── feature/
    ├── feature-home/             # Home screen with feature grid
    ├── feature-scanner/          # Document scanner (ML Kit)
    ├── feature-pdf-tools/        # PDF compression & unlock
    ├── feature-image-tools/      # Image compression
    ├── feature-calculators/      # SIP, EMI, Tax calculators
    ├── feature-dictionary/       # Word definitions
    ├── feature-utilities/        # ASCII, World Clock, RCS Cleaner
    └── feature-settings/         # App settings & preferences
```

### Technology Stack

| Layer | Technology |
|-------|------------|
| **UI** | Jetpack Compose, Material 3 |
| **DI** | Hilt (Dagger) |
| **Navigation** | Navigation Compose |
| **Async** | Kotlin Coroutines, Flow |
| **Storage** | DataStore (Preferences) |
| **PDF** | Apache PDFBox Android |
| **Scanning** | Google ML Kit Document Scanner |
| **Image Loading** | Coil |

---

## 📋 Implementation Plan

### Phase 1: Foundation ✅
- [x] Project setup with multi-module architecture
- [x] Core UI module with theme and components
- [x] Navigation structure with Compose Navigation
- [x] Hilt dependency injection setup

### Phase 2: Core Features ✅
- [x] Home screen with feature grid
- [x] Document Scanner with ML Kit integration
- [x] PDF Tools (Compress, Remove Password)
- [x] Image Compressor with quality control

### Phase 3: Utilities ✅
- [x] Financial Calculators (SIP, EMI, Tax)
- [x] World Clock with multiple time zones
- [x] ASCII Table reference
- [x] Dictionary lookup

### Phase 4: Advanced Features ✅
- [x] RCS/SMS Message Cleaner with bulk deletion
- [x] Default SMS App integration for message management
- [x] Settings with persistent preferences (DataStore)
- [x] Theme switching (Dark/Light/System)

### Phase 5: Polish ✅
- [x] FileProvider integration for secure file sharing
- [x] Open, Share, Rename, Close actions for all file outputs
- [x] Default quality/compression settings propagation
- [x] UI refinements (grid spacing, tile sizes)

---

## 🚀 Task Breakdown

### Completed Tasks

| Task | Module | Status |
|------|--------|--------|
| Create SwissGradientCard component | core-ui | ✅ |
| Create SwissButton component | core-ui | ✅ |
| Create SwissCard component | core-ui | ✅ |
| Implement dark/light theme | core-ui | ✅ |
| Setup HomeScreen with grid | feature-home | ✅ |
| Implement ML Kit scanner | feature-scanner | ✅ |
| Add PDF/scan result actions | feature-scanner | ✅ |
| Implement PDF compression | feature-pdf-tools | ✅ |
| Implement password removal | feature-pdf-tools | ✅ |
| Add FileProvider for sharing | app | ✅ |
| Implement image compression | feature-image-tools | ✅ |
| Add SIP Calculator | feature-calculators | ✅ |
| Add EMI Calculator | feature-calculators | ✅ |
| Add Tax Calculator | feature-calculators | ✅ |
| Create ASCII Table | feature-utilities | ✅ |
| Create World Clock | feature-utilities | ✅ |
| Implement RCS Cleaner | feature-utilities | ✅ |
| Add Default SMS App capability | feature-utilities | ✅ |
| Bulk message deletion | feature-utilities | ✅ |
| Settings with DataStore | feature-settings | ✅ |
| Theme preference persistence | feature-settings | ✅ |
| Default quality settings | feature-settings | ✅ |
| Link settings to tools | Multiple | ✅ |

---

## 🔐 Permissions

```xml
<!-- SMS Permissions (for RCS Cleaner) -->
<uses-permission android:name="android.permission.READ_SMS"/>
<uses-permission android:name="android.permission.WRITE_SMS"/>
<uses-permission android:name="android.permission.SEND_SMS"/>
<uses-permission android:name="android.permission.RECEIVE_SMS"/>
<uses-permission android:name="android.permission.RECEIVE_MMS"/>
<uses-permission android:name="android.permission.RECEIVE_WAP_PUSH"/>
<uses-permission android:name="android.permission.READ_CELL_BROADCASTS"/>

<!-- Camera (for Document Scanner) -->
<uses-permission android:name="android.permission.CAMERA"/>
```

---

## 🛠️ Building

```bash
# Clone the repository
git clone https://github.com/meghr/SwissArmy.git

# Open in Android Studio
# OR build via command line

./gradlew assembleDebug

# APK will be at: app/build/outputs/apk/debug/app-debug.apk
```

---

## 📸 Screenshots

*Coming soon*

---

## 📄 License

This project is for educational and personal use.

---

## 👨‍💻 Author

**Meghr Sharma**

---

*Built with ❤️ using Kotlin and Jetpack Compose*
