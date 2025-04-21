pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
        mavenLocal()
    }
}

rootProject.name = "convention-plugins"

include(":common-conventions")
include(":gradle-plugin-conventions")
