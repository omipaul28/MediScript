# 🩺 Smart E-Prescription Mobile App

![Platform](https://img.shields.io/badge/platform-Android-green)
![Language](https://img.shields.io/badge/language-Kotlin-blue)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-orange)
![Backend](https://img.shields.io/badge/backend-Firebase-yellow)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

A modern, offline-first Android application that helps doctors create, manage, and share digital prescriptions efficiently—built במיוחד for real-world clinical environments.

---

## 📱 Features

### 🔐 Core

* Secure doctor authentication (Firebase)
* Patient management (add, search, update)
* Digital prescription creation
* Prescription history & reuse

### 🚀 Advanced

* 🎤 **Voice Input**

  * Dictate prescriptions using speech recognition
* 💡 **Smart Suggestions**

  * Auto-complete medicine names
  * Suggest frequently used medicines
* 📡 **Offline-First System**

  * Works without internet
  * Auto-sync when online
* 📄 **PDF Generation**

  * Professional prescription layout
* 📤 **Easy Sharing**

  * Share via WhatsApp / SMS / PDF
* 🎨 **Modern UI**

  * Built with Jetpack Compose
  * Fast and minimal interaction

---

## 🧠 Why This Project?

Traditional prescription systems:

* ❌ Hard to read (handwriting issues)
* ❌ No digital records
* ❌ Time-consuming
* ❌ Not reusable

Existing apps:

* ❌ Require constant internet
* ❌ Too complex
* ❌ Heavy typing

✅ This app solves all of the above with a **doctor-centric, lightweight design**. 

---

## 🏗️ Architecture

```
UI (Jetpack Compose)
        ↓
     ViewModel
        ↓
   Repository Layer
        ↓
 ┌───────────────┬───────────────┐
 | Local (Room)  | Cloud (Firebase) |
 └───────────────┴───────────────┘
        ↓
   Sync Engine (WorkManager)
```

---

## 🛠️ Tech Stack

| Layer             | Technology                        |
| ----------------- | --------------------------------- |
| Language          | Kotlin                            |
| UI                | Jetpack Compose, Material 3       |
| Backend           | Firebase Auth, Firestore, Storage |
| Local Database    | Room (SQLite)                     |
| Background Tasks  | WorkManager                       |
| Voice Recognition | Android SpeechRecognizer API      |
| PDF Generation    | Android PdfDocument               |

---

## 🚀 Getting Started

### ✅ Prerequisites

* Android Studio (latest version)
* Android SDK (API 24+ recommended)
* Firebase project

---

### 🔧 Installation

1. **Clone the repository**

```bash
git clone https://github.com/omipaul28/MediScript
cd MediScript
```

2. **Open in Android Studio**

3. **Setup Firebase**

* Go to Firebase Console
* Create a new project
* Add Android app
* Download `google-services.json`
* Place it inside `/app` folder

4. **Sync Gradle**

* Click *Sync Project with Gradle Files*

5. **Run the app**

* Connect device/emulator
* Click ▶ Run

---

## 🔐 Permissions Required

* Internet (for sync)
* Microphone (for voice input)
* Storage (for PDF generation)

---

## 📸 Screenshots (Add yours)

```
<img width="738" height="1600" alt="WhatsApp Image 2026-04-21 at 10 52 11 AM" src="https://github.com/user-attachments/assets/b99cf2f9-d43f-4a01-a087-3a4d7ed3acc2" />
<img width="738" height="1600" alt="WhatsApp Image 2026-04-21 at 10 52 11 AM (1)" src="https://github.com/user-attachments/assets/8e16e942-71ac-4fe0-a177-a6bad0f6eee6" />
<img width="738" height="1600" alt="WhatsApp Image 2026-04-21 at 10 52 10 AM" src="https://github.com/user-attachments/assets/43b8e277-0e78-45e4-943b-6b88812bf49f" />
<img width="738" height="1600" alt="WhatsApp Image 2026-04-21 at 10 52 10 AM (2)" src="https://github.com/user-attachments/assets/61a9f7ae-775e-4849-9e1c-22fef1272de1" />
<img width="738" height="1600" alt="WhatsApp Image 2026-04-21 at 10 52 10 AM (1)" src="https://github.com/user-attachments/assets/4702fc77-6064-4b7b-86a1-d52b97021e3d" />

```

---

## 🌍 Use Cases

* Rural healthcare clinics
* Small private practices
* Low-internet environments
* Doctors needing fast workflows

---

## 🔮 Future Improvements

* 🤖 AI-based prescription suggestions
* 🌐 Multi-language support
* 💊 Pharmacy integration
* 👨‍⚕️ Patient mobile app

---


## 👨‍💻 Author

Omi Paul

* GitHub: [https://github.com/omipaul28](https://github.com/omipaul28)
* Email: [omipaul28@gmail.com](omipaul28@gmail.com)

