# Sylheti Translator — Android App

A native Android WebView app for [sylheti.kamildex.com](https://sylheti.kamildex.com) — a community-driven Sylheti to Bangla translation platform.

[![Get it on Google Play](https://img.shields.io/badge/Google%20Play-Download-green?logo=google-play)](https://play.google.com/store/apps/details?id=co.median.android.jlrnql)
[![Build](https://github.com/kamilhussen24/sylheti-translator-app/actions/workflows/build.yml/badge.svg)](https://github.com/kamilhussen24/sylheti-translator-app/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/kamilhussen24/sylheti-translator-app/pulls)

---

## About

Sylheti Translator is an open-source Android app that brings the Sylheti language translation experience to mobile. Built with native Kotlin, it wraps the web platform into a smooth Android experience.

This project is open to contributions from anyone — whether you're fixing a bug, improving the UI, or adding new features.

---

## Features

- Native Android WebView
- Splash screen with logo animation
- No internet detection with auto-reconnect
- Native share dialog integration
- Chrome Custom Tabs for external links
- Deep link support for `sylheti.kamildex.com`
- Automated CI/CD via GitHub Actions

---

## Getting Started

### Prerequisites
- Git
- GitHub account

### No Android Studio needed!

This project uses **GitHub Actions** to build automatically. You don't need Android Studio or any local setup.

1. **Fork** this repository
2. Make your changes directly on GitHub
3. Push to `main` — build starts automatically
4. Download the APK from the **Actions** tab

---

## Project Structure

```
sylheti-translator-app/
├── app/src/main/
│   ├── java/com/kamildex/sylheti/
│   │   ├── SplashActivity.kt       ← Splash screen
│   │   ├── MainActivity.kt         ← Main WebView screen
│   │   └── NoNetworkActivity.kt    ← No internet screen
│   ├── res/
│   │   ├── layout/                 ← UI layouts
│   │   ├── values/                 ← Colors, themes, strings
│   │   ├── drawable/               ← App logo
│   │   └── raw/                    ← Lottie animation
│   └── AndroidManifest.xml
├── .github/workflows/
│   └── build.yml                   ← Automated build pipeline
└── app/build.gradle                ← Version & dependencies
```

---

## How to Contribute

We welcome all contributions! Here's how:

1. **Fork** the repository
2. Create a new branch: `git checkout -b feature/your-feature`
3. Make your changes
4. Push and open a **Pull Request**

### What you can contribute:
- 🐛 Bug fixes
- 🎨 UI/UX improvements
- ✨ New features
- 📝 Documentation
- 🌐 Translations

---

## Common Updates

### Change website URL:
`MainActivity.kt` → `webUrl`

### Change share message:
`MainActivity.kt` → `shareApp()`

### Update version (required for Play Store):
`app/build.gradle`:
```groovy
versionCode 31        // Increase by 1
versionName "3.2.0"
```

### Change app colors:
`res/values/colors.xml`

---

## Web-App Bridge

The app communicates with the website using Android's `JavascriptInterface`:

**In website JavaScript:**
```javascript
Android.share();           // Trigger native share
Android.openUrl(url);      // Open URL in Chrome Custom Tab
```

**In Android (MainActivity.kt):**
```kotlin
addJavascriptInterface(AndroidBridge(this), "Android")
```

---

## App Info

| Property | Value |
|----------|-------|
| Package | `co.median.android.jlrnql` |
| Min SDK | 23 (Android 6.0+) |
| Target SDK | 34 (Android 14) |
| Language | Kotlin |

---

## Related Projects

- 🌐 **Website:** [sylheti.kamildex.com](https://sylheti.kamildex.com)
- 📂 **Website Source:** [github.com/kamilhussen24/sylheti-translator](https://github.com/kamilhussen24/sylheti-translator)

---

## License

MIT License — free to use, modify and distribute.

```
Copyright (c) 2025 Kamil Dex
```

---

## Developer

**Kamil Dex**  
🌐 [kamildex.com](https://kamildex.com)  
📧 Contributions and feedback welcome via Issues or Pull Requests.
