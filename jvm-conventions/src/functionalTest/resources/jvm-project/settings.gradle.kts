plugins {
    id("io.technoirlab.conventions.jvm-application") apply false
    id("io.technoirlab.conventions.jvm-library") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":jvm-application")
include(":jvm-library")
