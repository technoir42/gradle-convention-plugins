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

    functionalTestImplementation(testFixtures(project(":common-conventions")))
    functionalTestImplementation(libs.assertj.core)
}

gradlePlugin {
    plugins {
        register("jvmLibraryConventions") {
            id = "io.github.technoir42.conventions.jvm-library"
            implementationClass = "io.github.technoir42.conventions.jvm.library.JvmLibraryConventionPlugin"
        }
    }
}
