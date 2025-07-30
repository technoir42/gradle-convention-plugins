package io.github.technoir42.conventions.gradle.plugin

import io.github.technoir42.conventions.common.ProjectSettingsImpl
import io.github.technoir42.conventions.common.configureBuildConfig
import io.github.technoir42.conventions.common.configureCommon
import io.github.technoir42.conventions.common.configureDetekt
import io.github.technoir42.conventions.common.configureJava
import io.github.technoir42.conventions.common.configureKotlin
import io.github.technoir42.conventions.common.configureKotlinSerialization
import io.github.technoir42.conventions.common.configurePublishing
import io.github.technoir42.conventions.common.configureTestFixtures
import io.github.technoir42.conventions.common.configureTesting
import io.github.technoir42.conventions.gradle.plugin.api.GradlePluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

class GradlePluginConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val config = extensions.create<GradlePluginExtension>(GradlePluginExtension.NAME)
        config.initDefaults(project.name)

        afterEvaluate {
            configureBuildConfig(config.buildFeatures.buildConfig, config.packageName)
            configureKotlinSerialization(config.buildFeatures.serialization)
        }

        pluginManager.apply("org.gradle.kotlin.kotlin-dsl")

        val projectSettings = ProjectSettingsImpl(this)
        configureCommon(projectSettings)
        configureJava()
        configureKotlin(KotlinVersion.KOTLIN_1_8)
        configureDetekt()
        configurePublishing()
        configurePlugin()
        configureTesting()
        configureTestFixtures()
    }
}
