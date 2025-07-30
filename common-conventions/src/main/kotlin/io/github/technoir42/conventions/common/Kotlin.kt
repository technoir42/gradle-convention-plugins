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
import java.io.File
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
    val includePath = providers.environmentVariable("INCLUDE").map { it.split(File.pathSeparator) }
    val libraryPath = providers.environmentVariable("LIB").map { it.split(File.pathSeparator) }

    configure<KotlinMultiplatformExtension> {
        compilerOptions {
            optIn.add("kotlinx.cinterop.ExperimentalForeignApi")
        }

        linuxX64 {
            afterEvaluate {
                configureFromDsl(executable, packageName, enableCInterop)
            }
        }
        macosArm64 {
            afterEvaluate {
                configureFromDsl(executable, packageName, enableCInterop)
            }
        }
        mingwX64 {
            afterEvaluate {
                configureFromDsl(executable, packageName, enableCInterop)
            }
        }

        targets.withType<KotlinNativeTarget>().configureEach {
            compilations.configureEach {
                cinterops.configureEach {
                    val srcPath = Path("src", "nativeInterop", "cinterop")
                    compilerOpts("-I$srcPath")
                    compilerOpts(includePath.orNull?.map { "-I$it" }.orEmpty())
                }
            }
            binaries.configureEach {
                linkerOpts(libraryPath.orNull?.map { "-L$it" }.orEmpty())
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
