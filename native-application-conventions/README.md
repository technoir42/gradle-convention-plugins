Native application conventions
==============================

## Usage

```kotlin
plugins {
    id("io.github.technoir42.conventions.native-application")
}

nativeApplication {
    // The base package name
    packageName = "com.example.native.application"

    // Optional build features
    buildFeatures {
        // Generate BuildConfig
        buildConfig = true
        // Enable Kotlin serialization
        serialization = true
    }
}
```
