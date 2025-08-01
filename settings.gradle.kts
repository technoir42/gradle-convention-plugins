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
        val conventionPluginsVersion = "v12"
        id("io.github.technoir42.conventions.gradle-plugin") version conventionPluginsVersion
        id("io.github.technoir42.conventions.jvm-application") version conventionPluginsVersion
        id("io.github.technoir42.conventions.jvm-library") version conventionPluginsVersion
        id("io.github.technoir42.conventions.settings") version conventionPluginsVersion
    }
}

plugins {
    id("io.github.technoir42.conventions.gradle-plugin") apply false
    id("io.github.technoir42.conventions.jvm-library") apply false
    id("io.github.technoir42.conventions.settings")
}

globalSettings {
    projectId = "convention-plugins"
}

include(":gradle-extensions")
include(":common-conventions")
include(":gradle-plugin-conventions")
include(":jvm-application-conventions")
include(":jvm-library-conventions")
include(":native-application-conventions")
include(":native-library-conventions")
include(":settings-conventions")
