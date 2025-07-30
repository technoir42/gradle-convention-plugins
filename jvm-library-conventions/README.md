JVM library conventions
=======================

## Usage

```kotlin
plugins {
    id("io.github.technoir42.conventions.jvm-library")
}

jvmLibrary {
    // The base package name
    packageName = "com.example.jvm.library"

    // Optional build features
    buildFeatures {
        // Generate BuildConfig
        buildConfig = true
        // Enable Kotlin serialization
        serialization = true
    }
}
```
