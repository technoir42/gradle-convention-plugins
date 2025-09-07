package io.github.technoir42.conventions.gradle.plugin

import io.github.technoir42.gradle.dependencies.api
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.plugin.devel.tasks.ValidatePlugins
import org.gradle.testing.base.TestingExtension

internal fun Project.configurePlugin() {
    extensions.configure(JavaPluginExtension::class) {
        val apiSourceSet = sourceSets.create(API_VARIANT_NAME)

        // TODO: Make 'api' artifact work with ABI validation
        registerFeature(API_VARIANT_NAME) {
            withSourcesJar()
            usingSourceSet(apiSourceSet)
            capability("$group", "$name-$API_VARIANT_NAME", "$version")
        }
    }

    @Suppress("UnstableApiUsage")
    extensions.configure(TestingExtension::class) {
        val functionalTestSuite = suites.register("functionalTest", JvmTestSuite::class) {
            dependencies {
                implementation.add(project())
                implementation.add(gradleTestKit())
            }
        }
        tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME).configure {
            dependsOn(functionalTestSuite)
        }
        extensions.configure(GradlePluginDevelopmentExtension::class) {
            testSourceSet(functionalTestSuite.get().sources)
        }
    }

    dependencies {
        apiImplementation(gradleApi())
        api(project(path)) {
            capabilities {
                requireCapability("$group:$name-$API_VARIANT_NAME:$version")
            }
        }
    }

    tasks.withType<ValidatePlugins>().configureEach {
        enableStricterValidation.set(true)
    }
}

private fun DependencyHandlerScope.apiImplementation(dependencyNotation: Any): Dependency? =
    "${API_VARIANT_NAME}Implementation"(dependencyNotation)

private const val API_VARIANT_NAME = "api"
