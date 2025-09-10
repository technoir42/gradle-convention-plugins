package io.github.technoir42.conventions.kotlin.multiplatform

import io.github.technoir42.conventions.common.ProjectSettingsImpl
import io.github.technoir42.conventions.common.configureBuildConfig
import io.github.technoir42.conventions.common.configureCommon
import io.github.technoir42.conventions.common.configureDetekt
import io.github.technoir42.conventions.common.configureKotlinSerialization
import io.github.technoir42.conventions.common.configurePublishing
import io.github.technoir42.conventions.kotlin.multiplatform.api.KotlinMultiplatformLibraryExtension
import io.github.technoir42.gradle.Environment
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class KotlinMultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val config = extensions.create<KotlinMultiplatformLibraryExtension>(KotlinMultiplatformLibraryExtension.NAME)
        config.initDefaults(project.name)

        afterEvaluate {
            configureBuildConfig(config.buildFeatures.buildConfig, config.packageName)
            configureKotlinSerialization(config.buildFeatures.serialization)
        }

        val environment = Environment(providers)
        val projectSettings = ProjectSettingsImpl(this)
        configureCommon(projectSettings)
        configureKotlinMultiplatform(
            packageName = config.packageName,
            defaultTargets = config.defaultTargets,
            buildFeatures = config.buildFeatures
        )
        configureDetekt()
        configurePublishing(publicationName = "kotlinMultiplatform", environment)
    }
}
