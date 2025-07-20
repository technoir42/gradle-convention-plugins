package io.github.technoir42.conventions.common

import io.github.technoir42.gradle.dependencies.implementation
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

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

fun Project.configureKotlinMultiplatform(packageName: Provider<String>, executable: Boolean = false) {
    configure<KotlinMultiplatformExtension> {
        linuxX64()
        macosArm64()
        mingwX64()
    }

    afterEvaluate {
        configure<KotlinMultiplatformExtension> {
            linuxX64 {
                configureBinaries(executable, packageName)
            }
            macosArm64 {
                configureBinaries(executable, packageName)
            }
            mingwX64 {
                configureBinaries(executable, packageName)
            }
        }
    }
}

private fun KotlinNativeTarget.configureBinaries(executable: Boolean, packageName: Provider<String>) {
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
