Gradle plugin conventions
=========================

## Usage

```kotlin
plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

gradlePluginConfig {
    // The base package name
    packageName = "com.example.gradle.plugin"

    // Optional build features
    buildFeatures {
        // Generate BuildConfig
        buildConfig = true
        // Enable Kotlin serialization
        serialization = true
    }
}
```
