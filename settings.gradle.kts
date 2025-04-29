pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
        maven(providers.gradleProperty("mavenRepositoryUrl")) {
            credentials(PasswordCredentials::class)
        }
    }
    plugins {
        val conventionPluginsVersion = "0.0.4"
        id("io.github.technoir42.conventions.gradle-plugin") version conventionPluginsVersion
        id("io.github.technoir42.conventions.jvm-library") version conventionPluginsVersion
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
include(":jvm-library-conventions")
include(":settings-conventions")
