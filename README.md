# Auto WiFi Login for University Students

## Overview
This project is an open-source Android application designed to help university students automatically log in to their WiFi networks that require Captive Portal authentication. With this app, users no longer have to repeatedly enter their credentials, as it automates the login process, saving time and effort.

## Features
- **Open Source**: This app is open for contributions. Anyone can join and help improve the project by submitting permissions and contributing code.
- **Automatic Login**: The app assists students by automatically filling in saved credentials and submitting the login form without manual intervention.
- **Secure Credentials**: User credentials are stored locally in an Android Room Database, ensuring data security and privacy (not stored on any server).

## How It Works
1. **Initial Setup**:
   - When the app is first launched, users need to enter their WiFi credentials.
   - The credentials are saved securely in an Android Room Database.
   
2. **Automatic Login**:
   - The app detects the login page and, using JavaScript, auto-fills the saved credentials and submits the form. Users don't need to enter credentials or press any buttons.
   - The app can close automatically after a successful login if the "Auto die" option is checked. If not checked, users can manually close the app using the Exit button.

3. **Notification and Login**:
   - Whenever the user receives a "Signup required" notification, opening the app will automatically log them in with saved credentials.
   - If "Auto die" was checked during initial setup, the app will close automatically after logging in.

**Note**: Ensure your mobile network is turned off during the login process to maintain a stable connection with the WiFi network.

## Steps to Use
1. **Download or Clone**:
   - [Download the APK](https://drive.google.com/file/d/10hoD9G8d7iYdCpwPGNMaQF2TJNayeMM2/view?usp=sharing) or clone the project into Android Studio and build the app.
2. **Install the App**:
   - Install the APK on your device.
3. **Initial Setup**:
   - Enter your WiFi credentials when prompted. These credentials will be saved in the local Android Room Database.
   - Optionally, check "Auto die" if you want the app to close automatically after a successful login.
   - Click "Save & Login" to store your credentials and log in.
4. **Using the App**:
   - When you see the "Signup required" notification, open the app, and it will automatically log you in.
   - If "Auto die" was enabled, the app will close automatically after login.

## 🤝 Contributing
We welcome contributions to this project! If you have ideas or improvements, feel free to contribute by following the repository's contribution guidelines.

## License
This project is licensed under the [MIT License](LICENSE).

## 🙌 Acknowledgements
Thank you to all contributors and the open-source community for their support.

## Contact
For any inquiries or support, please contact me at **rahmaniyashekh@gmail.com**.

## ⭐ Support the Project
If you find this project helpful, consider giving it a star on GitHub to show your support!
