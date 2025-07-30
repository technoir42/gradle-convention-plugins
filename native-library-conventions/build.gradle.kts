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
    implementation(libs.kotlin.gradle.plugin)

    functionalTestImplementation(testFixtures(project(":common-conventions")))
}

gradlePlugin {
    plugins {
        register("nativeLibraryConventions") {
            id = "io.github.technoir42.conventions.native-library"
            implementationClass = "io.github.technoir42.conventions.native.library.NativeLibraryConventionPlugin"
        }
    }
}
