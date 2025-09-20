package io.github.technoir42.conventions.settings

import io.github.technoir42.conventions.common.api.CommonExtension
import io.github.technoir42.conventions.common.api.metadata.ProjectMetadata
import io.github.technoir42.conventions.settings.api.SettingsExtension
import io.github.technoir42.gradle.Environment
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create

@Suppress("UnstableApiUsage")
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

        gradle.lifecycle.beforeProject {
            configureMetadata(config.metadata)
        }

        gradle.lifecycle.afterProject {
            configureDependencyResolution()
        }

        val environment = Environment(providers)
        configureDependencyAnalysis()
        configureDependencyResolution(environment)
        configureDevelocity(environment)
        configurePublishing()
    }

    private fun Project.configureMetadata(metadata: ProjectMetadata) {
        pluginManager.withPlugin("io.github.technoir42.conventions.common") {
            extensions.configure(CommonExtension::class) {
                this.metadata.initWith(metadata)
            }
        }
    }

    private fun ProjectMetadata.initWith(other: ProjectMetadata) {
        name.convention(other.name)
        description.convention(other.description)
        url.convention(other.url)
        licenses.convention(other.licenses)
        developers.convention(other.developers)
    }
}
