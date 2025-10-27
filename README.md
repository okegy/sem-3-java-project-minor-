# CyberLearn — JavaFX Desktop Cybersecurity Suite

A beautiful, student-friendly cybersecurity learning platform that bundles **four tools** (Encryption, Port Scanner, Integrity Checker, Password Manager) with **Courses + Quiz + Progress** — all in a neon cyberpunk UI.

## ✨ Features
- 🔐 AES-GCM **Encryption/Decryption** for text
- 📡 **Port Scanner** (concurrent) to find open ports
- 🧾 **File Integrity** hashes (SHA-256, MD5)
- 🔑 **Password Manager** with an encrypted local vault
- 📚 **Courses** notes (Level 1–5)
- 🧠 **Quiz** with score tracking

> Tagline: *“You are the boss of your digital realm.”*

## 🗂 Project Structure
```
cyberlearn-javafx/
  ├─ pom.xml
  ├─ data/                   # your vaults, exports, etc.
  └─ src/
     ├─ main/java/com/cyberlearn/app/
     │  ├─ MainApp.java
     │  ├─ controller/
     │  │   ├─ MainController.java
     │  │   ├─ EncryptionController.java
     │  │   ├─ PortScannerController.java
     │  │   ├─ HashController.java
     │  │   ├─ PasswordManagerController.java
     │  │   ├─ CoursesController.java
     │  │   └─ QuizController.java
     │  ├─ model/
     │  │   ├─ PasswordEntry.java
     │  │   └─ QuizQuestion.java
     │  └─ util/
     │      ├─ CryptoUtil.java
     │      ├─ HashUtil.java
     │      ├─ PortScanner.java
     │      └─ Vault.java
     └─ main/resources/com/cyberlearn/app/
        ├─ css/cyberpunk.css
        └─ fxml/
           ├─ main.fxml
           ├─ encryption.fxml
           ├─ portscanner.fxml
           ├─ hash.fxml
           ├─ password.fxml
           ├─ courses.fxml
           └─ quiz.fxml
```

## 🔧 Prerequisites
- Java 17+
- Maven 3.8+

## ▶️ Run
```bash
mvn clean javafx:run
```
If you use an IDE, mark `src/main/resources` as Resources and run `MainApp`.

## 📚 Learning Guide (Inside the App)
- **Level 1: Cyber Basics Explorer** — password hygiene, phishing, safe browsing → use **Password Manager**
- **Level 2: Network Gatekeeper** — IP/ports & firewalls → use **Port Scanner**
- **Level 3: Data Protector** — encryption keys → use **Encryption**
- **Level 4: Integrity Guardian** — hashing & tamper detection → use **Integrity**
- **Level 5: Incident Detective** — combine all tools for triage

## 🛡 Security Notes
- AES keys are derived with PBKDF2; encryption uses **AES/GCM** (nonce + tag)
- Vault is a single encrypted JSON file: remember your **master password**
- Hashing uses Java `MessageDigest`

## 🖌 UI
- JavaFX + FXML + CSS in **neon cyberpunk** style
- Clear spacing, hover effects, progress & badges placeholders

## 🧪 Extend for Bonus Marks
- Export scan results to CSV
- Password strength meter & generator
- Light/Dark theme toggle
- Activity logs, cloud backup for vault

---

Made for your **CyberLearn** project submission. Shine bright ✨


## 🔐 Login & Roles
- Launches at **Login** screen.
- Default **Admin**: `admin / admin123` (auto-created on first run)
- **Students** can **Create Account** from login.

## 👑 Admin Panel
- View **Users** and **Progress**.
- **Export CSV** of progress.
- **Generate Reports** → saved in `/reports` as one file per student.

## 📈 Progress Tracked
- Quiz score & attempts
- Tool usage counts: Encryption, Port Scanner, Integrity, Password Manager

