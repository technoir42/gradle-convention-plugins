JVM library conventions
=======================

## Usage

```kotlin
plugins {
    id("io.github.technoir42.conventions.jvm-library")
}

jvmLibrary {
    // Optional build features
    buildFeatures {
        // Enable Kotlin serialization
        serialization = true
    }
}
```
