package io.github.technoir42.conventions.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.withType
import org.gradle.plugin.devel.tasks.ValidatePlugins

internal fun Project.configurePlugin() {
    configure<JavaPluginExtension> {
        val apiSourceSet = sourceSets.create(API_VARIANT_NAME)

        registerFeature(API_VARIANT_NAME) {
            withSourcesJar()
            usingSourceSet(apiSourceSet)
            capability("$group", "$name-api", "$version")
        }
    }

    dependencies {
        "apiImplementation"(gradleApi())
        "api"(project(path)) {
            capabilities {
                requireCapability("$group:$name-api:$version")
            }
        }
    }

    tasks.withType<ValidatePlugins>().configureEach {
        enableStricterValidation.set(true)
    }
}

private const val API_VARIANT_NAME = "api"
