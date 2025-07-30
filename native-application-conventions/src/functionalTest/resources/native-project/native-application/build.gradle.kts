plugins {
    id("io.github.technoir42.conventions.native-application")
}

nativeApplication {
    buildFeatures {
        cinterop = true
    }
}
