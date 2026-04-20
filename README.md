# 🌾 Farm Assist

**Farm Assist** is an advanced, offline-first Android application tailor-made as a dynamic decision-support system for farmers, specifically catering to South Indian agricultural patterns. Embracing a modern technology stack, it provides critical insights, calculates yields, curates live news, and drives sustainable agriculture right to a farmer's fingertips.

---

## 📱 Features

Farm Assist heavily emphasizes usability, speed, and robust offline availability when out in the fields. Included in its ecosystem are:

*   **🌱 Crop Estimation & Advisory**: Recommends crops dynamically based on regional soil types and real-time hyperlocal weather data (provided by OpenWeatherMap API).
*   **🚜 Crop Maintenance Tracking**: A full digital calendar for scheduling irrigation, weeding, and fertilizing. Comes equipped with animated descriptions for step-by-step guidance.
*   **📰 Live Agricultural News (Offline-First)**: Fetches the latest agribusiness news using a public REST API. By caching network responses into a Room database, updates are accessible even when completely offline.
*   **🏙️ Terrace Farming Guide**: Detailed analytics on daily sunlight requirements, container sizing, and harvesting tips for urban farmers.
*   **💰 Financial & Yield Calculators**: Computes cost investments versus expected acre yields based on historical algorithms.
*   **♻️ Waste Management**: Recommends safe and sustainable farm-waste recycling techniques to produce biochar, composed, or biofuels tailored directly to the crops you own.
*   **🏛️ Government Schemes**: An integrated offline directory listing financial schemes, criteria, and benefits.
*   **🔒 Secure Profiles**: Multi-tenant application with Guest support, custom PIN setups, and multi-language capability setups configured natively.

---

## 🛠️ Technology Stack

Built with adherence to Google's Android architecture best practices.

*   **Language:** Kotlin
*   **UI Toolkit:** Jetpack Compose (Material Design 3 Theme, Animations, Gradient Glassmorphism)
*   **Architecture Pattern:** MVVM (Model-View-ViewModel) + Repository Pattern
*   **Local Persistence (Offline Cache):** Room Database + KSP (Kotlin Symbol Processing) pre-populated with a mass local CSV dataset
*   **Network & Dependency:** Retrofit2 + Gson for News & Weather API parsing
*   **Asynchrony & Reactive:** Kotlin Coroutines & Flows
*   **Background Tasks:** WorkManager
*   **Location Services:** Google Play Services API Location Helper
*   **Build Tool:** Gradle (Kotlin DSL properties)

---

## 🚀 Getting Started

### Prerequisites
*   Android Studio Ladybug (or newer recommended for Jetpack Compose)
*   An Android Device or API 24+ Emulator (Android 7.0 Nougat Desktop)
*   A valid [OpenWeatherMap API Key](https://openweathermap.org/) (for weather advisory integration)

### Installation

1.  **Clone the repository**
    ```bash
    git clone https://github.com/your-username/FarmAssist.git
    cd FarmAssist
    ```

2.  **Add your API Keys**
    *   Navigate to your local navigation configuration (e.g., `NavGraph.kt` or `local.properties`).
    *   Replace placeholder keys string for OpenWeatherMap with your active API Key. (News API operates dynamically under a free-unauthenticated endpoint).

3.  **Build and Run**
    *   Open the project via Android Studio.
    *   Sync Gradle files (`File > Sync Project with Gradle Files`).
    *   Ensure a solid initial internet connection for Gradle to pull Compose BOM & libraries.
    *   Click **Run 'app'** (`Shift + F10`) to deploy to your configured device.

---

## 🏗 Architecture Structure

The application separates internal layers cleanly to facilitate unit testing and decoupled development:

```
com.farmassist
│
├── data/              # Core Repository and Storage layer
│   ├── local/         # Room Database Entities, DAOs, and Data Seeding models
│   ├── remote/        # Retrofit APIs, network interceptors, and DTO implementations
│   └── repository/    # Local + Remote Synchronisation layer caches
│
├── domain/            # Complex calculators and algorithmic recommendation engines
│
├── ui/                # UI Presentation (Jetpack Compose based)
│   ├── navigation/    # App Navigation Graph, routing handlers
│   ├── screens/       # Application screens (Dashboard, News, Crop)
│   ├── viewmodels/    # Lifecycle-aware data managers mapped to Views 
│   └── theme/         # Color palettes, typography configs, tokens
│
└── util/              # Multi-Language interceptors, GPS mappers, TTS services
```

---

## 🤝 Contributing

Contributions, issues, and feature requests are always welcome! 

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

---

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.
