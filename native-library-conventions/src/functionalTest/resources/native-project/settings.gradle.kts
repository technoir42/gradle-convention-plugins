plugins {
    id("io.github.technoir42.conventions.native-library") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":native-library")
