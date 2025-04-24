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
        val conventionPluginsVersion = "0.0.2"
        id("io.github.technoir42.conventions.gradle-plugin") version conventionPluginsVersion
        id("io.github.technoir42.conventions.settings") version conventionPluginsVersion
    }
}

plugins {
    id("io.github.technoir42.conventions.settings")
}

globalSettings {
    projectId = "convention-plugins"
}

include(":common-conventions")
include(":gradle-plugin-conventions")
include(":settings-conventions")
