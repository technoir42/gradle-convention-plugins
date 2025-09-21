plugins {
    id("io.technoirlab.conventions.gradle-plugin") apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":example-plugin")
