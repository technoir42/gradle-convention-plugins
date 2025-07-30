Native library conventions
==========================

## Usage

```kotlin
plugins {
    id("io.github.technoir42.conventions.native-library")
}

nativeLibrary {
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
