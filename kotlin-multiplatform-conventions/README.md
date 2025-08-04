Native application conventions
==============================

## Usage

```kotlin
plugins {
    id("io.github.technoir42.conventions.kotlin-multiplatform-application")
}

kotlinMultiplatformApplication {
    // The base package name
    packageName = "com.example.native.application"

    // Optional build features
    buildFeatures {
        // Generate BuildConfig
        buildConfig = true
        // Enable Kotlin serialization
        serialization = true
        // Enable C interop
        cinterop = true
    }
}
```

## Usage

```kotlin
plugins {
    id("io.github.technoir42.conventions.kotlin-library")
}

kotlinLibrary {
    // The base package name
    packageName = "com.example.native.library"

    // Optional build features
    buildFeatures {
        // Generate BuildConfig
        buildConfig = true
        // Enable Kotlin serialization
        serialization = true
        // Enable C interop
        cinterop = true
    }
}
```
