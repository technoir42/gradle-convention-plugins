package io.github.technoir42.conventions.gradle.plugin

import io.github.technoir42.conventions.common.api.ProjectSettings
import io.github.technoir42.conventions.common.configureCommon
import io.github.technoir42.conventions.common.configureKotlin
import io.github.technoir42.conventions.common.configurePublishing
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

class GradlePluginConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.apply("org.gradle.kotlin.kotlin-dsl")

        val projectSettings = ProjectSettings(this)
        configureCommon(projectSettings)
        configureKotlin(KotlinVersion.KOTLIN_1_8)
        configurePublishing()
        configurePlugin()
    }
}
