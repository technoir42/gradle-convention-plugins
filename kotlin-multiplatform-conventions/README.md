Kotlin multiplatform conventions
================================

## KMP Application

```kotlin
plugins {
    id("io.github.technoir42.conventions.kotlin-multiplatform-application")
}

kotlinMultiplatformApplication {
    // The base package name
    packageName = "com.example.native.application"

    // Optional build features
    buildFeatures {
        // Enable ABI validation
        abiValidation = true
        // Generate BuildConfig
        buildConfig = true
        // Enable Kotlin serialization
        serialization = true
        // Enable C interop
        cinterop = true
    }
}
```

## KMP Library

```kotlin
plugins {
    id("io.github.technoir42.conventions.kotlin-multiplatform-library")
}

kotlinLibrary {
    // The base package name
    packageName = "com.example.native.library"

    // Optional build features
    buildFeatures {
        // Enable ABI validation
        abiValidation = true
        // Generate BuildConfig
        buildConfig = true
        // Enable Kotlin serialization
        serialization = true
        // Enable C interop
        cinterop = true
    }
}
```
