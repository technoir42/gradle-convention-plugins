pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(providers.gradleProperty("mavenRepositoryUrl")) {
            credentials(PasswordCredentials::class)
        }
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
        mavenLocal()
    }
}

rootProject.name = "convention-plugins"

include(":common-conventions")
include(":gradle-plugin-conventions")
