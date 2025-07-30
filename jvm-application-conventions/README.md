JVM application conventions
===========================

## Usage

```kotlin
plugins {
    id("io.github.technoir42.conventions.jvm-application")
}

jvmApplication {
    // The base package name
    packageName = "com.example.jvm.application"

    // The name of the application's main class
    mainClass = ".MainKt"

    // The list of string arguments to pass to the JVM when running the application
    jvmArgs = listOf("-Xmx512m")

    // Optional build features
    buildFeatures {
        // Generate BuildConfig
        buildConfig = true
        // Enable Kotlin serialization
        serialization = true
    }
}
```
