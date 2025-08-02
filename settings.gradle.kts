pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
        maven("https://maven.pkg.github.com/technoir42/gradle-convention-plugins") {
            credentials {
                username = providers.gradleProperty("gpr.user").orNull
                password = providers.gradleProperty("gpr.token").orNull
            }
        }
    }
    plugins {
        val conventionPluginsVersion = "v14"
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

dependencyResolutionManagement {
    repositories {
        maven("https://maven.pkg.github.com/technoir42/gradle-convention-plugins") {
            credentials {
                username = providers.gradleProperty("gpr.user").orNull
                password = providers.gradleProperty("gpr.token").orNull
            }
        }
    }
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
