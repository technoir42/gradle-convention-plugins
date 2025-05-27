Settings conventions
====================

## Usage

```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        // Maven repository where the plugins are published
        maven("https://maven.pkg.github.com/technoir42/gradle-convention-plugins") {
            credentials(PasswordCredentials::class)
        }
    }
    plugins {
        val conventionPluginsVersion = "<version>"
        id("io.github.technoir42.conventions.settings") version conventionPluginsVersion
    }
}

plugins {
    id("io.github.technoir42.conventions.settings")
}

globalSettings {
    // Required: Unique project ID
    projectId = "my-project"
}
```
