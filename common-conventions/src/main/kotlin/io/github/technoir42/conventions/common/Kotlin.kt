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
import java.io.File

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
    val includePath = providers.environmentVariable("INCLUDE").map { it.split(File.pathSeparator) }
    val libraryPath = providers.environmentVariable("LIB").map { it.split(File.pathSeparator) }

    configure<KotlinMultiplatformExtension> {
        linuxX64()
        macosArm64()
        mingwX64()

        afterEvaluate {
            linuxX64 {
                configureTarget(executable, packageName, includePath, libraryPath)
            }
            macosArm64 {
                configureTarget(executable, packageName, includePath, libraryPath)
            }
            mingwX64 {
                configureTarget(executable, packageName, includePath, libraryPath)
            }
        }
    }
}

private fun KotlinNativeTarget.configureTarget(
    executable: Boolean,
    packageName: Provider<String>,
    includePath: Provider<List<String>>,
    libraryPath: Provider<List<String>>
) {
    compilations.configureEach {
        cinterops.configureEach {
            compilerOpts(includePath.orNull?.map { "-I$it" }.orEmpty())
        }
    }

    binaries {
        if (executable) {
            executable {
                if (packageName.isPresent) {
                    entryPoint = "${packageName.get()}.main"
                }

                linkerOpts(libraryPath.orNull?.map { "-L$it" }.orEmpty())
            }
        }
    }
}
