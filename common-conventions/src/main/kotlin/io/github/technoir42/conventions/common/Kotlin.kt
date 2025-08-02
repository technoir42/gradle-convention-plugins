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
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
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

fun Project.configureKotlinMultiplatform(packageName: Provider<String>, enableCInterop: Property<Boolean>, executable: Boolean = false) {
    configure<KotlinMultiplatformExtension> {
        compilerOptions {
            optIn.add("kotlinx.cinterop.ExperimentalForeignApi")
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }

        // Tier 1
        iosArm64 {
            afterEvaluate { configureFromDsl(executable, packageName, enableCInterop) }
        }
        iosSimulatorArm64 {
            afterEvaluate { configureFromDsl(executable, packageName, enableCInterop) }
        }
        macosArm64 {
            afterEvaluate { configureFromDsl(executable, packageName, enableCInterop) }
        }
        // Tier 2
        linuxX64 {
            afterEvaluate { configureFromDsl(executable, packageName, enableCInterop) }
        }
        // Tier 3
        androidNativeArm64 {
            afterEvaluate { configureFromDsl(executable, packageName, enableCInterop) }
        }
        mingwX64 {
            afterEvaluate { configureFromDsl(executable, packageName, enableCInterop) }
        }

        targets.withType<KotlinNativeTarget>().configureEach {
            compilations.configureEach {
                cinterops.configureEach {
                    val srcPath = Path("src", "nativeInterop", "cinterop")
                    compilerOpts("-I$srcPath")
                }
            }
        }
    }
}

private fun KotlinNativeTarget.configureFromDsl(executable: Boolean, packageName: Provider<String>, enableCInterop: Provider<Boolean>) {
    if (enableCInterop.get()) {
        compilations.named("main") {
            cinterops.register(project.name) {
                packageName(packageName.get())
            }
        }
    }

    if (executable) {
        binaries {
            executable {
                if (packageName.isPresent) {
                    entryPoint = "${packageName.get()}.main"
                }
            }
        }
    }
}
