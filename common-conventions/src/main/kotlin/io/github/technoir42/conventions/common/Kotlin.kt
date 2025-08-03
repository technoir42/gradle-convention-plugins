package io.github.technoir42.conventions.common

import io.github.technoir42.gradle.dependencies.implementation
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinNativeTargetConfigurator.Companion.RUN_GROUP
import org.jetbrains.kotlin.gradle.plugin.mpp.Executable
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.HostManager
import kotlin.io.path.Path

fun Project.configureKotlin(kotlinVersion: KotlinVersion = KotlinVersion.DEFAULT) {
    configure<KotlinJvmProjectExtension> {
        compilerOptions {
            apiVersion.set(kotlinVersion)
            languageVersion.set(kotlinVersion)
        }
    }

    dependencies {
        implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
        implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:${DependencyVersions.KOTLINX_COROUTINES}"))
        implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:${DependencyVersions.KOTLINX_SERIALIZATION}"))
    }
}

fun Project.configureKotlinMultiplatform(
    packageName: Provider<String>,
    defaultTargets: Provider<Boolean>,
    enableCInterop: Property<Boolean>,
    executable: Boolean = false
) {
    afterEvaluate {
        if (defaultTargets.get()) {
            configure<KotlinMultiplatformExtension> {
                // Tier 1
                iosArm64()
                iosSimulatorArm64()
                macosArm64()
                // Tier 2
                linuxX64()
                // Tier 3
                androidNativeArm64()
                mingwX64()
            }
        }
    }

    pluginManager.apply("org.jetbrains.kotlin.multiplatform")

    configure<KotlinMultiplatformExtension> {
        compilerOptions {
            optIn.add("kotlinx.cinterop.ExperimentalForeignApi")
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }

        targets.withType<KotlinNativeTarget>().configureEach {
            configureTarget(packageName, enableCInterop, executable)
        }
    }
}

private fun KotlinNativeTarget.configureTarget(packageName: Provider<String>, enableCInterop: Property<Boolean>, executable: Boolean) {
    if (enableCInterop.get()) {
        compilations.named(KotlinCompilation.MAIN_COMPILATION_NAME) {
            cinterops.register(project.name) {
                packageName(packageName.get())
                val srcPath = Path("src", "nativeInterop", "cinterop")
                compilerOpts("-I$srcPath")
            }
        }
    }
    binaries {
        if (executable) {
            executable {
                if (packageName.isPresent) {
                    entryPoint = "${packageName.get()}.main"
                }
            }
        }
        if (HostManager.host == konanTarget) {
            withType<Executable>().configureEach {
                val executableName = name
                project.tasks.register("run${executableName.capitalized()}") {
                    group = RUN_GROUP
                    description = "Executes Kotlin/Native executable $executableName for target ${target.name}"
                    dependsOn(runTaskName!!)
                }
            }
        }
    }
}
