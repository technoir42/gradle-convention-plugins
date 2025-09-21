plugins {
    id("io.technoirlab.conventions.kotlin-multiplatform-application") apply false
    id("io.technoirlab.conventions.kotlin-multiplatform-library") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":kmp-application")
include(":kmp-library")
