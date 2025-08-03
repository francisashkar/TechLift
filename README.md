# TechLift App

TechLift is a mobile application designed to provide users with a seamless experience for learning and skill development. The app features user registration, login, structured learning roadmaps, interactive lessons, and quizzes, all backed by Firebase for secure authentication and data management.

## Features

- **Splash Screen**: Welcomes users with the app logo.
- **User Authentication**: Registration and login with form validation.
- **Roadmaps**: Structured learning paths to guide user progress.
- **Lessons & Quizzes**: Interactive modules and assessments.
- **Progress Tracking**: Monitor your learning journey.
- **Firebase Integration**: 
  - Authentication via Firebase Auth.
  - User data storage in Firestore.

## Tech Stack

- **Languages**: Kotlin, Java, JavaScript, TypeScript
- **Frameworks & Tools**: 
  - Android SDK
  - Firebase (Auth, Firestore)
  - Gradle
  - Node.js (for backend, if applicable)
- **UI Components**: Material Design (MaterialButton, TextInputLayout, etc.)

## Project Structure

```
app/
└── src/
    └── main/
        ├── java/com/example/techlift/
        │   ├── SplashActivity.kt
        │   ├── RegisterActivity.kt
        │   ├── ... (other activities)
        └── res/
            ├── layout/
            ├── drawable/
            └── values/
build.gradle.kts
```

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/francisashkar/TechLift.git
   cd TechLift
   ```

2. **Open in Android Studio**
   - Import the project as an existing Android project.

3. **Configure Firebase**
   - In the [Firebase Console](https://console.firebase.google.com/), create a project.
   - Download the `google-services.json` file and place it in `app/src/main/`.

4. **Build & Run**
   - Use Android Studio to build and run the app on your device or emulator.

## Contributing

Contributions are welcome! Please open issues or submit pull requests for new features, bug fixes, or improvements.

## License

This project is licensed under the MIT License.

---

**Contact:**  
For questions or support, please [open an issue](https://github.com/francisashkar/TechLift/issues).