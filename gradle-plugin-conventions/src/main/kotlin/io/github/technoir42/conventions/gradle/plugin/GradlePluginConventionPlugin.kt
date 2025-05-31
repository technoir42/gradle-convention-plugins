package io.github.technoir42.conventions.gradle.plugin

import io.github.technoir42.conventions.common.api.ProjectSettings
import io.github.technoir42.conventions.common.configureCommon
import io.github.technoir42.conventions.common.configureDetekt
import io.github.technoir42.conventions.common.configureJava
import io.github.technoir42.conventions.common.configureKotlin
import io.github.technoir42.conventions.common.configurePublishing
import io.github.technoir42.conventions.gradle.plugin.api.GradlePluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

class GradlePluginConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val config = extensions.create<GradlePluginExtension>(GradlePluginExtension.NAME)

        pluginManager.apply("org.gradle.kotlin.kotlin-dsl")

        val projectSettings = ProjectSettings(this)
        configureCommon(projectSettings)
        configureJava()
        configureKotlin(KotlinVersion.KOTLIN_1_8, enableSerialization = config.buildFeatures.serialization)
        configureDetekt()
        configurePublishing()
        configurePlugin()
    }
}
