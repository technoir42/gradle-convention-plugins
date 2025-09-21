plugins {
    id("io.technoirlab.conventions.jvm-library")
}

jvmLibrary {
    buildFeatures {
        abiValidation = true
    }
}

dependencies {
    implementation(gradleTestKit())
    implementation(libs.junit.jupiter.api)
}
