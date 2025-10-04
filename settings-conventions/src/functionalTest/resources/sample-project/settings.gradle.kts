plugins {
    id("io.technoirlab.conventions.settings")
}
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

globalSettings {
    projectId = "sample-project"
}

include(":jvm-library")
