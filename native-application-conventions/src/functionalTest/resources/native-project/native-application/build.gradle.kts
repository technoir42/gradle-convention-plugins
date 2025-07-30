import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.nio.file.Paths

plugins {
    id("io.github.technoir42.conventions.native-application")
}

kotlin {
    targets.withType<KotlinNativeTarget>().configureEach {
        compilations.named("main") {
            cinterops {
                create("lib") {
                    val cinteropPath = Paths.get("src", "nativeInterop", "cinterop")
                    definitionFile = file(cinteropPath.resolve("lib.def"))
                    compilerOpts("-I$cinteropPath")
                }
            }
        }
    }
}
