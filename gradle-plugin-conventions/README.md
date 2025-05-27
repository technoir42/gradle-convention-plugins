Gradle plugin conventions
=========================

## Usage

```kotlin
plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

gradlePluginConfig {
    buildFeatures {
        // Enable Kotlin serialization
        serialization = true
    }
}
```
