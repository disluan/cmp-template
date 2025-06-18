# Compose Multiplatform Firebase Template

This is a **Compose Multiplatform** project template with **Firebase Phone Authentication** support for both **Android** and **iOS** platforms.

Follow the steps below to set up the environment properly for development and testing.

---

## 🚀 Setup Instructions

### 🔧 Prerequisites

- **Android Studio Narwhal or newer**
- **Xcode 15 or newer**
- **CocoaPods installed**
- A **Firebase project** with **Phone Authentication** enabled

---

### 1. Generate Firebase Configuration Files

Go to [Firebase Console](https://console.firebase.google.com/) and:

#### 📱 For Android
- Add a new Android app to your Firebase project.
- Download the `google-services.json` file.
- Place it into:

  ```
  composeApp/google-services.json
  ```

#### 🍏 For iOS
- Add a new iOS app to your Firebase project.
- Download the `GoogleService-Info.plist` file.
- Rename and place it as follows:

  ```
  iosApp/Firebase/Dev-GoogleService-Info.plist    // For Debug
  iosApp/Firebase/Prd-GoogleService-Info.plist    // For Release
  ```

---

### 2. Update Firebase Path in iOS Config

Edit this file:

```
iosApp/Configuration/Config.xcconfig
```

Add the following lines:

```xcconfig
GOOGLE_SERVICE[config=Debug]=$(SRCROOT)/Firebase/Dev-GoogleService-Info.plist
GOOGLE_SERVICE[config=Release]=$(SRCROOT)/Firebase/Prd-GoogleService-Info.plist
```

---

### 3. Set Your Apple Developer Team ID

Update the **TEAM_ID** in Xcode project settings or inside:

```
iosApp/Configuration/Config.xcconfig
```

Replace the placeholder with your actual Apple Developer Team ID.

---

### 4. Configure URL Types in Info.plist

Firebase Phone Auth on iOS requires URL types to be set for reCAPTCHA verification.

- Open `iosApp/Info.plist`
- Add a `URL type` entry using your Firebase reversed client ID.

Refer to:  
🔗 https://firebase.google.com/docs/auth/ios/phone-auth#set-up-recaptcha-verification

Make sure to use **URL encoding** if needed.

---

### 5. Install CocoaPods

Navigate to the iOS directory and run:

```bash
cd iosApp
pod install
```

> ⚠️ Always open the Xcode project using the `.xcworkspace` file, **not** `.xcodeproj`.

---

### 6. Open in Android Studio

Open the root project folder in **Android Studio Narwhal or newer** for further development.

---

## 📂 Project Structure

```
project-root/
├── composeApp/                 # Android app
│   └── google-services.json    # Firebase config (Android)
├── iosApp/                     # iOS app
│   ├── Firebase/               # Firebase config (iOS)
│   └── Configuration/          # Xcode build settings
├── firebsae/                   # Firebase module
└── build.gradle.kts, etc.      # Project configuration files
```

---

## 📌 Features

- ✅ Firebase Phone Auth (Android & iOS)
- 🧑‍💻 Kotlin Multiplatform Shared Logic
- 🎨 Compose UI (Multiplatform)
- ⚙️ Environment-based Firebase config

---

## 🤝 Contributions

Contributions are welcome!  
Feel free to fork this repository, open issues, or submit pull requests.

---