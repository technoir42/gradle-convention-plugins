pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(providers.gradleProperty("mavenRepositoryUrl"))
        mavenLocal()
    }
    plugins {
        id("io.github.technoir42.conventions.gradle-plugin") version "0.0.1"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
        maven(providers.gradleProperty("mavenRepositoryUrl"))
        mavenLocal()
    }
}

rootProject.name = "convention-plugins"

include(":common-conventions")
include(":gradle-plugin-conventions")
