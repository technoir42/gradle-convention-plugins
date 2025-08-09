package io.github.technoir42.conventions.settings

import io.github.technoir42.conventions.settings.api.SettingsExtension
import io.github.technoir42.gradle.Environment
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.create

class SettingsConventionPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) = with(settings) {
        pluginManager.apply("com.autonomousapps.build-health")
        pluginManager.apply("com.gradle.develocity")
        pluginManager.apply("org.gradle.toolchains.foojay-resolver-convention")

        val config = extensions.create<SettingsExtension>(SettingsExtension.NAME)

        gradle.settingsEvaluated {
            rootProject.name = config.projectId.get()
        }

        gradle.rootProject {
            pluginManager.apply("org.gradle.lifecycle-base")
        }

        gradle.afterProject {
            configureDependencyResolution()
        }

        val environment = Environment(providers)
        configureDependencyAnalysis()
        configureDependencyResolution(environment)
        configureDevelocity(environment)
    }
}
