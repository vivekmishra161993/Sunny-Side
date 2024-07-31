# Sunny Side Weather Forecast Application

Sunny Side is an Android application that provides weekly, daily, and current weather forecasts using the OpenWeather API. The application is built using Jetpack Compose and follows Clean Architecture principles. It utilizes the latest version cataloging for dependencies in Gradle.

## Features

- **Current Weather**: Get the current temperature and weather conditions based on your location.
- **Daily Forecast**: View weather forecasts for the next few days.
- **Weekly Forecast**: Check the weather forecast for the entire week.
- **Modern UI**: Built with Jetpack Compose for a smooth and modern user interface.
- **Clean Architecture**: Ensures maintainable and testable codebase.

![Screenshot_1722447136](https://github.com/user-attachments/assets/0b3093c0-8185-4409-bbe6-548ce3fb0550)

## Tech Stack

- **Language**: Kotlin
- **Architecture**: Clean Architecture
- **UI**: Jetpack Compose
- **Dependency Injection**: Hilt
- **Networking**: Retrofit, OkHttp3
- **Asynchronous Programming**: Coroutines
- **Image Loading**: Coil
- **API**: OpenWeather API
- **Build System**: Gradle with version cataloging

## Libraries Used

- [androidx.core:core-ktx](https://developer.android.com/jetpack/androidx/releases/core)
- [androidx.lifecycle:lifecycle-runtime-ktx](https://developer.android.com/jetpack/androidx/releases/lifecycle)
- [androidx.activity:activity-compose](https://developer.android.com/jetpack/androidx/releases/activity)
- [androidx.compose.bom](https://developer.android.com/jetpack/compose/bom)
- [androidx.ui](https://developer.android.com/jetpack/compose)
- [androidx.ui.graphics](https://developer.android.com/jetpack/compose)
- [androidx.ui.tooling.preview](https://developer.android.com/jetpack/compose)
- [androidx.material3](https://developer.android.com/jetpack/androidx/releases/material3)
- [com.google.dagger:hilt-android](https://dagger.dev/hilt/)
- [com.squareup.retrofit2:retrofit](https://square.github.io/retrofit/)
- [com.squareup.okhttp3:okhttp](https://square.github.io/okhttp/)
- [com.squareup.okhttp3:logging-interceptor](https://square.github.io/okhttp/)
- [org.jetbrains.kotlinx:kotlinx-coroutines-core](https://github.com/Kotlin/kotlinx.coroutines)
- [org.jetbrains.kotlinx:kotlinx-coroutines-android](https://github.com/Kotlin/kotlinx.coroutines)
- [androidx.core:core-splashscreen](https://developer.android.com/jetpack/androidx/releases/core-splashscreen)
- [io.coil-kt:coil](https://coil-kt.github.io/coil/)
- [androidx.compose.runtime:runtime-compose](https://developer.android.com/jetpack/compose/runtime)
- [com.squareup.retrofit2:converter-gson](https://square.github.io/retrofit/)
- [com.google.code.gson:gson](https://github.com/google/gson)

## Getting Started

### Prerequisites

- Android Studio Flamingo or later
- Gradle 8.0 or later

### Installation

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/sunny-side.git]
   ```
2. **Open the project in Android Studio.**

3. **Sync the project with Gradle files.**

4. **Run the application on your Android device or emulator.**

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/your-feature`
3. Make your changes and commit them: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgements

- [OpenWeather](https://openweathermap.org/) for the weather API.
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for the UI toolkit.
- [Hilt](https://dagger.dev/hilt/) for dependency injection.
- [Retrofit](https://square.github.io/retrofit/) for networking.
- [Coil](https://coil-kt.github.io/coil/) for image loading.
