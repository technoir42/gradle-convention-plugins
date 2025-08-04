plugins {
    id("io.github.technoir42.conventions.kotlin-multiplatform-application") apply false
    id("io.github.technoir42.conventions.kotlin-multiplatform-library") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":kmp-application")
include(":kmp-library")
