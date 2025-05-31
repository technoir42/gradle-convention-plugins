package io.github.technoir42.conventions.jvm.application

import io.github.technoir42.conventions.common.api.ProjectSettings
import io.github.technoir42.conventions.common.configureCommon
import io.github.technoir42.conventions.common.configureDetekt
import io.github.technoir42.conventions.common.configureJava
import io.github.technoir42.conventions.common.configureKotlin
import io.github.technoir42.conventions.common.configureTesting
import io.github.technoir42.conventions.jvm.application.api.JvmApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class JvmApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val config = extensions.create<JvmApplicationExtension>(JvmApplicationExtension.NAME)

        pluginManager.apply("application")
        pluginManager.apply("org.jetbrains.kotlin.jvm")

        val projectSettings = ProjectSettings(this)
        configureCommon(projectSettings)
        configureJava()
        configureApplication(config)
        configureKotlin(enableSerialization = config.buildFeatures.serialization)
        configureDetekt()
        configureTesting()
    }
}
