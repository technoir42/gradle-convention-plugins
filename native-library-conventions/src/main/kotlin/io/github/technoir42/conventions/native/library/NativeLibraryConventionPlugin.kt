package io.github.technoir42.conventions.native.library

import io.github.technoir42.conventions.common.configureBuildConfig
import io.github.technoir42.conventions.common.configureDetekt
import io.github.technoir42.conventions.common.configureKotlinMultiplatform
import io.github.technoir42.conventions.common.configureKotlinSerialization
import io.github.technoir42.conventions.native.library.api.NativeLibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class NativeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val config = extensions.create<NativeLibraryExtension>(NativeLibraryExtension.NAME)
        config.initDefaults(project.name)

        afterEvaluate {
            configureBuildConfig(config.buildFeatures.buildConfig, config.packageName)
            configureKotlinSerialization(config.buildFeatures.serialization)
        }

        pluginManager.apply("org.jetbrains.kotlin.multiplatform")

        configureKotlinMultiplatform(config.packageName)
        configureDetekt()
    }
}
