plugins {
    id("io.technoirlab.conventions.kotlin-multiplatform-application")
}

kotlinMultiplatformApplication {
    buildFeatures {
        cinterop = true
    }
}

kotlin {
    linuxX64()
    macosArm64()
    mingwX64()
}
