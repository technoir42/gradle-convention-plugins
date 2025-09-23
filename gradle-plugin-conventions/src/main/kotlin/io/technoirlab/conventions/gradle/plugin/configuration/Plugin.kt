package io.technoirlab.conventions.gradle.plugin.configuration

import io.technoirlab.conventions.common.api.metadata.ProjectMetadata
import io.technoirlab.gradle.Environment
import io.technoirlab.gradle.dependencies.api
import io.technoirlab.gradle.setDisallowChanges
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.plugin.devel.tasks.ValidatePlugins
import org.gradle.testing.base.TestingExtension
import org.jetbrains.dokka.gradle.DokkaExtension

internal fun Project.configurePlugin(metadata: ProjectMetadata, environment: Environment) {
    configureApiVariant(API_VARIANT_NAME)

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
            website.setDisallowChanges(metadata.url)
            vcsUrl.setDisallowChanges(environment.vcsUrl)

            plugins.configureEach {
                displayName = metadata.name.orNull
                description = metadata.description.orNull
            }

            testSourceSet(functionalTestSuite.get().sources)
        }
    }

    tasks.withType<ValidatePlugins>().configureEach {
        enableStricterValidation.set(true)
    }
}

private fun Project.configureApiVariant(variantName: String) {
    extensions.configure(JavaPluginExtension::class) {
        val apiSourceSet = sourceSets.create(variantName)

        // TODO: Make 'api' artifact work with ABI validation
        registerFeature(variantName) {
            withSourcesJar()
            usingSourceSet(apiSourceSet)
            capability("$group", "$name-$variantName", "$version")
        }

        if (pluginManager.hasPlugin("org.jetbrains.dokka")) {
            extensions.configure(DokkaExtension::class) {
                dokkaSourceSets.named("main") {
                    sourceRoots.from(apiSourceSet.allSource.srcDirs)
                    classpath.from(apiSourceSet.compileClasspath)
                }
            }
        }
    }

    dependencies {
        "${variantName}Implementation"(gradleApi())
        api(project(path)) {
            capabilities {
                requireCapability("$group:$name-$variantName")
            }
        }
    }
}

private const val API_VARIANT_NAME = "api"
