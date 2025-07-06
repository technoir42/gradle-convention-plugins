plugins {
    id("io.github.technoir42.conventions.gradle-plugin")
}

dependencies {
    apiApi(project(":common-conventions")) {
        capabilities {
            requireCapability("${project.group}:common-conventions-api")
        }
    }
    implementation(project(":common-conventions"))
    implementation(libs.kotlin.gradle.plugin.api)
}

gradlePlugin {
    plugins {
        register("jvmApplicationConventions") {
            id = "io.github.technoir42.conventions.jvm-application"
            implementationClass = "io.github.technoir42.conventions.jvm.application.JvmApplicationConventionPlugin"
        }
    }
}
