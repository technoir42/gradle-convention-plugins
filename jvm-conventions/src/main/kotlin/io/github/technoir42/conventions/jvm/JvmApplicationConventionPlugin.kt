package io.github.technoir42.conventions.jvm

import io.github.technoir42.conventions.common.ProjectSettingsImpl
import io.github.technoir42.conventions.common.configureBuildConfig
import io.github.technoir42.conventions.common.configureCommon
import io.github.technoir42.conventions.common.configureDetekt
import io.github.technoir42.conventions.common.configureJava
import io.github.technoir42.conventions.common.configureKotlin
import io.github.technoir42.conventions.common.configureKotlinSerialization
import io.github.technoir42.conventions.common.configureTesting
import io.github.technoir42.conventions.jvm.api.JvmApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create

class JvmApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        val config = extensions.create<JvmApplicationExtension>(JvmApplicationExtension.NAME)
        config.initDefaults(project.name)

        afterEvaluate {
            configureBuildConfig(config.buildFeatures.buildConfig, config.packageName)
            configureKotlinSerialization(config.buildFeatures.serialization)
        }

        pluginManager.apply("application")
        pluginManager.apply("org.jetbrains.kotlin.jvm")

        val projectSettings = ProjectSettingsImpl(this)
        configureCommon(projectSettings)
        configureJava()
        configureApplication(config)
        configureKotlin(config.buildFeatures.abiValidation)
        configureDetekt()
        configureTesting()
    }

    private fun Project.configureApplication(config: JvmApplicationExtension) {
        configure<JavaApplication> {
            mainClass.set(config.fullyQualifiedMainClass)

            afterEvaluate {
                applicationDefaultJvmArgs = config.jvmArgs.get()
            }
        }
    }

    private val JvmApplicationExtension.fullyQualifiedMainClass: Provider<String>
        get() = packageName.zip(mainClass) { packageName, mainClass ->
            if (mainClass.startsWith(".")) {
                "$packageName$mainClass"
            } else {
                mainClass
            }
        }
            .orElse(mainClass)
}
