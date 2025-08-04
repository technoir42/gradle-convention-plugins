package io.github.technoir42.conventions.jvm

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
import io.github.technoir42.conventions.jvm.api.JvmLibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val config = extensions.create<JvmLibraryExtension>(JvmLibraryExtension.NAME)
        config.initDefaults(project.name)

        afterEvaluate {
            configureBuildConfig(config.buildFeatures.buildConfig, config.packageName)
            configureKotlinSerialization(config.buildFeatures.serialization)
        }

        pluginManager.apply("java-library")
        pluginManager.apply("org.jetbrains.kotlin.jvm")

        val projectSettings = ProjectSettingsImpl(this)
        configureCommon(projectSettings)
        configureJava()
        configureKotlin()
        configureDetekt()
        configurePublishing(isLibrary = true)
        configureTesting()
        configureTestFixtures()
    }
}
