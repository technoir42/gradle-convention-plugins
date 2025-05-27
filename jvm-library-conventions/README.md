JVM library conventions
=======================

## Usage

```kotlin
plugins {
    id("io.github.technoir42.conventions.jvm-library")
}

jvmLibrary {
    buildFeatures {
        // Enable Kotlin serialization
        serialization = true
    }
}
```
