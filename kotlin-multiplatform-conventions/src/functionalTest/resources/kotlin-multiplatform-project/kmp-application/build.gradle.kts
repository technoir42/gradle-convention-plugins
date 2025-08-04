plugins {
    id("io.github.technoir42.conventions.kotlin-multiplatform-application")
}

kotlinMultiplatformApplication {
    buildFeatures {
        cinterop = true
    }
}
