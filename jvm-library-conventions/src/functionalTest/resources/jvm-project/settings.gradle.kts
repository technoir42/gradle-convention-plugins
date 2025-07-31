plugins {
    id("io.github.technoir42.conventions.jvm-library") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":jvm-library")
