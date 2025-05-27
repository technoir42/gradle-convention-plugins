JVM application conventions
===========================

## Usage

```kotlin
plugins {
    id("io.github.technoir42.conventions.jvm-application")
}

jvmApplication {
    // The fully qualified name of the application's main class.
    mainClass = "com.example.MainKt"

    // The list of string arguments to pass to the JVM when running the application.
    jvmArgs = listOf("-Xmx512m")

    // Optional build features
    buildFeatures {
        // Enable Kotlin serialization
        serialization = true
    }
}
```
