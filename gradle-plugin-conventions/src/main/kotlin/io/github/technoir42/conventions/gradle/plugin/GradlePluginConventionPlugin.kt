package io.github.technoir42.conventions.gradle.plugin

import io.github.technoir42.conventions.common.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

class GradlePluginConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.apply("org.gradle.kotlin.kotlin-dsl")

        configureKotlin(KotlinVersion.KOTLIN_1_8)
    }
}
