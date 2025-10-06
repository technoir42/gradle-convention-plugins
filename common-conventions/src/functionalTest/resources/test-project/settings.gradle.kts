plugins {
    id("io.technoirlab.conventions.common") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

include(":library1")
include(":library2")
