Kotlin multiplatform conventions
================================

## KMP Application

```kotlin
plugins {
    id("io.technoirlab.conventions.kotlin-multiplatform-application")
}

kotlinMultiplatformApplication {
    // The base package name
    packageName = "com.example.kmp.application"

    // Whether to create the default targets.
    defaultTargets = true

    // Optional build features
    buildFeatures {
        // Enable ABI validation
        abiValidation = true
        // Enable Metro dependency injection
        metro = true
        // Enable Kotlin serialization
        serialization = true
        // Enable C interop
        cinterop = true

        // Configuration of `BuildConfig` class generation
        buildConfig {
            // Add a String field
            buildConfigField("STRING_FIELD", "string value")
            // Add a variant-specific field
            buildConfigField("TEST_STRING_FIELD", "string value", variant = "test")
        }
    }
}
```

## KMP Library

```kotlin
plugins {
    id("io.technoirlab.conventions.kotlin-multiplatform-library")
}

kotlinLibrary {
    // The base package name
    packageName = "com.example.kmp.library"

    // Whether to create the default targets.
    defaultTargets = true

    // Optional build features
    buildFeatures {
        // Enable ABI validation
        abiValidation = true
        // Enable Metro dependency injection
        metro = true
        // Enable Kotlin serialization
        serialization = true
        // Enable C interop
        cinterop = true

        // Configuration of `BuildConfig` class generation
        buildConfig {
            // Add a String field
            buildConfigField("STRING_FIELD", "string value")
            // Add a variant-specific field
            buildConfigField("TEST_STRING_FIELD", "string value", variant = "test")
        }
    }
}
```
