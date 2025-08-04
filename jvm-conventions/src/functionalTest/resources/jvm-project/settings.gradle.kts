plugins {
    id("io.github.technoir42.conventions.jvm-application") apply false
    id("io.github.technoir42.conventions.jvm-library") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":jvm-application")
include(":jvm-library")
