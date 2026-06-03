# 🌟 Sylheti Translator — Android App

[![Build Status](https://github.com/YOUR_USERNAME/sylheti-translator/actions/workflows/build.yml/badge.svg)](https://github.com/YOUR_USERNAME/sylheti-translator/actions)

WebView-based Android app for [sylheti.kamildex.com](https://sylheti.kamildex.com/)

---

## 📱 Features

- ✅ Splash screen with Lottie animation
- ✅ WebView with full website experience
- ✅ No internet screen with animation + auto-reconnect
- ✅ Share button integration
- ✅ Deep link support
- ✅ Chrome Custom Tabs for external links
- ✅ Back navigation (WebView history aware)

---

## 🚀 How to Build (Mobile থেকেও করা যাবে!)

### প্রতিটা `main` push এ automatically build হয়:
- 📱 **Debug APK** — testing এর জন্য
- 📦 **Release APK** — direct install
- 🏪 **Release AAB** — Play Store upload

### Build দেখতে:
`GitHub → Actions tab → সবচেয়ে নতুন run → Artifacts`

---

## ⚙️ First Time Setup — GitHub Secrets

Repository Settings → Secrets → Actions → New secret:

| Secret Name | Value |
|-------------|-------|
| `KEYSTORE_BASE64` | `keystore_base64.txt` এর সম্পূর্ণ content |
| `STORE_PASSWORD` | `android` |
| `KEY_ALIAS` | `androidkey` |
| `KEY_PASSWORD` | `android` |

> ⚠️ `keystore_base64.txt` GitHub এ push করবে না! শুধু Secrets এ দাও।

---

## 📁 Project Structure

```
sylheti-translator/
├── app/
│   ├── src/main/
│   │   ├── java/co/median/android/jlrnql/
│   │   │   ├── SplashActivity.kt      ← Splash + network check
│   │   │   ├── MainActivity.kt        ← WebView + share + deeplink
│   │   │   └── NoNetworkActivity.kt   ← No internet screen
│   │   ├── res/
│   │   │   ├── raw/
│   │   │   │   ├── animation.json          ← Splash Lottie
│   │   │   │   └── no_net_animation.json   ← No-net Lottie
│   │   │   └── ...
│   │   └── AndroidManifest.xml
│   ├── keystore/
│   │   └── android.keystore   ← (GitHub Actions এ inject হয়)
│   └── build.gradle
├── .github/workflows/
│   └── build.yml              ← Build pipeline
└── README.md
```

---

## 🔄 App Update করতে

যেকোনো change করে `main` branch এ push দাও — automatically নতুন build ready!

### URL পরিবর্তন করতে:
`MainActivity.kt` → `webUrl` variable

### Version বাড়াতে:
`app/build.gradle` → `versionCode` ও `versionName`

---

## 📊 App Info

| বিষয় | তথ্য |
|------|-----|
| Package | `co.median.android.jlrnql` |
| Min SDK | 23 (Android 6.0+) |
| Target SDK | 34 (Android 14) |
| Version | 3.1.0 |

---

## 🏪 Play Store Update

1. AAB artifact download করো
2. Play Console → Production → New release
3. AAB upload করো
4. Submit!
