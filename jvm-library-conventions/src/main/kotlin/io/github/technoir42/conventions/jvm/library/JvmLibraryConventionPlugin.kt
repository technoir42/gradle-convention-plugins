package io.github.technoir42.conventions.jvm.library

import io.github.technoir42.conventions.common.api.ProjectSettings
import io.github.technoir42.conventions.common.configureCommon
import io.github.technoir42.conventions.common.configureKotlin
import io.github.technoir42.conventions.common.configurePublishing
import io.github.technoir42.conventions.jvm.library.api.JvmLibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val config = extensions.create<JvmLibraryExtension>(JvmLibraryExtension.NAME)

        pluginManager.apply("org.jetbrains.kotlin.jvm")

        val projectSettings = ProjectSettings(this)
        configureCommon(projectSettings)
        configureKotlin(enableSerialization = config.buildFeatures.serialization)
        configurePublishing(isLibrary = true)
    }
}
