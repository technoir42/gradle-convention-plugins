plugins {
    id("io.github.technoir42.conventions.jvm-application") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":jvm-application")
