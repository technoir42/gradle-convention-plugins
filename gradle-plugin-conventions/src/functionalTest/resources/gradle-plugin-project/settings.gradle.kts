plugins {
    id("io.github.technoir42.conventions.gradle-plugin") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":gradle-plugin")
