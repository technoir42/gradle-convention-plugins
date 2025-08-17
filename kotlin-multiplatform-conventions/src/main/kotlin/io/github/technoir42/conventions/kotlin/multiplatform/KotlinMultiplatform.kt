package io.github.technoir42.conventions.kotlin.multiplatform

import io.github.technoir42.conventions.common.CommonDependencies
import io.github.technoir42.conventions.common.capitalized
import io.github.technoir42.conventions.common.configureCompilerOptions
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinNativeTargetConfigurator.Companion.RUN_GROUP
import org.jetbrains.kotlin.gradle.plugin.mpp.Executable
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.konan.target.HostManager
import kotlin.io.path.Path

internal fun Project.configureKotlinMultiplatform(
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
            optIn.addAll(
                "kotlin.time.ExperimentalTime",
                "kotlinx.cinterop.ExperimentalForeignApi"
            )
            freeCompilerArgs.addAll(
                "-Xcontext-parameters",
                "-Xconsistent-data-class-copy-visibility",
                "-Xexpect-actual-classes",
                "-Xnested-type-aliases",
            )
        }

        targets.configureEach {
            when (this) {
                is KotlinAndroidTarget -> configureCompilerOptions()
                is KotlinJvmTarget -> configureCompilerOptions()
                is KotlinNativeTarget -> configureNativeTarget(packageName, enableCInterop, executable)
            }
        }

        sourceSets {
            commonMain.dependencies {
                implementation(dependencies.platform(CommonDependencies.KOTLIN_BOM))
                implementation(dependencies.platform(CommonDependencies.KOTLINX_COROUTINES_BOM))
                implementation(dependencies.platform(CommonDependencies.KOTLINX_SERIALIZATION_BOM))
            }
        }
    }
}

private fun KotlinNativeTarget.configureNativeTarget(
    packageName: Provider<String>,
    enableCInterop: Property<Boolean>,
    executable: Boolean
) {
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
