plugins {
    id("io.github.technoir42.conventions.native-application") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":native-application")
