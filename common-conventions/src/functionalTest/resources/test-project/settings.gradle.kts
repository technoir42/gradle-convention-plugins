plugins {
    id("io.technoirlab.conventions.common") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":library")
