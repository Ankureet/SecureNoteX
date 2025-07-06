# 🔐 SecureNoteX – End-to-End Encrypted Note Sharing (Java GUI)

**SecureNoteX** is a secure and user-friendly desktop application built in Java that allows users to create, encrypt, and share notes using AES-256 encryption. Each note is encrypted locally and can only be decrypted using a unique Note ID and key, ensuring complete privacy and confidentiality.

---

## 🚀 Features

- 🔒 AES-256 encryption for strong local security
- 📝 Create and save encrypted notes with a single click
- 📤 Share notes using a unique Note ID and Base64 key
- 🔓 Decrypt received notes through a clean GUI
- 🖥️ Built with Java Swing – lightweight and fully offline

---

## 📦 How to Run

### Requirements:
- Java JDK 17 or above
- Any IDE (e.g., VS Code, IntelliJ) or terminal

### Steps:

```bash
git clone https://github.com/your-username/SecureNoteX.git
cd SecureNoteX
javac SecureNoteXGUI.java
java SecureNoteXGUI
