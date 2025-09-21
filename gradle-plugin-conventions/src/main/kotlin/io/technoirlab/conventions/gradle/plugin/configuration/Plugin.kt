package io.technoirlab.conventions.gradle.plugin.configuration

import io.technoirlab.conventions.common.api.metadata.ProjectMetadata
import io.technoirlab.gradle.Environment
import io.technoirlab.gradle.dependencies.api
import io.technoirlab.gradle.setDisallowChanges
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.attributes.java.TargetJvmEnvironment
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.plugin.devel.tasks.ValidatePlugins
import org.gradle.testing.base.TestingExtension
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

internal fun Project.configurePlugin(metadata: ProjectMetadata, environment: Environment) {
    extensions.configure(JavaPluginExtension::class) {
        val apiSourceSet = sourceSets.create(API_VARIANT_NAME)

        // TODO: Make 'api' artifact work with ABI validation
        registerFeature(API_VARIANT_NAME) {
            withSourcesJar()
            usingSourceSet(apiSourceSet)
            capability("$group", "$name-$API_VARIANT_NAME", "$version")
        }

        if (pluginManager.hasPlugin("org.jetbrains.dokka")) {
            extensions.configure(DokkaExtension::class) {
                dokkaSourceSets.named("main") {
                    sourceRoots.from(apiSourceSet.allSource.srcDirs)
                    classpath.from(apiSourceSet.compileClasspath)
                }
            }
        }

        // Align attributes with the main variant
        @Suppress("NoNameShadowing")
        configurations.named { it == apiSourceSet.apiElementsConfigurationName || it == apiSourceSet.runtimeElementsConfigurationName }
            .configureEach {
                attributes {
                    attribute(TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE, objects.named(TargetJvmEnvironment.STANDARD_JVM))
                    attribute(KotlinPlatformType.attribute, KotlinPlatformType.jvm)
                }
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
            website.setDisallowChanges(metadata.url)
            vcsUrl.setDisallowChanges(environment.vcsUrl)

            plugins.configureEach {
                displayName = metadata.name.orNull
                description = metadata.description.orNull
            }

            testSourceSet(functionalTestSuite.get().sources)
        }
    }

    dependencies {
        apiImplementation(gradleApi())
        api(project(path)) {
            capabilities {
                requireCapability("$group:$name-$API_VARIANT_NAME")
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
