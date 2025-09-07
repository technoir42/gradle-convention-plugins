package io.github.technoir42.conventions.common

import io.github.technoir42.gradle.capitalized
import org.gradle.api.Project
import org.gradle.api.attributes.Usage
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaExtension

fun Project.configurePublishing(
    publicationName: String,
    javadoc: Boolean = false,
    extraConfiguration: MavenPublication.() -> Unit = {}
) {
    if (javadoc) {
        pluginManager.apply("org.jetbrains.dokka-javadoc")
    } else {
        pluginManager.apply("org.jetbrains.dokka")
    }
    pluginManager.apply("maven-publish")

    extensions.configure(DokkaExtension::class) {
        dokkaPublications.configureEach {
            val publicationName = name
            tasks.register<Jar>("dokka${publicationName.capitalized()}Jar") {
                dependsOn(tasks.named("dokkaGeneratePublication${publicationName.capitalized()}"))
                from(outputDirectory)
                archiveClassifier.set(publicationName)
            }
        }
    }

    val projectDescription = provider { description }

    extensions.configure(PublishingExtension::class) {
        val publishUrl = providers.gradleProperty("publish.url")
        repositories {
            if (publishUrl.isPresent) {
                maven(publishUrl) {
                    if (url.scheme != "file") {
                        credentials {
                            username = providers.gradleProperty("publish.username").orNull
                            password = providers.gradleProperty("publish.password").orNull
                        }
                    }
                }
            }
        }

        val dokkaExtension = extensions.getByType(DokkaExtension::class)
        publications.withType<MavenPublication>().configureEach {
            pom {
                description.convention(projectDescription)
            }

            versionMapping {
                usage(Usage.JAVA_API) {
                    fromResolutionResult()
                }
                usage(Usage.JAVA_RUNTIME) {
                    fromResolutionResult()
                }
            }

            if (name == publicationName) {
                extraConfiguration()
            }
        }
    }

    afterEvaluate {
        extensions.configure(PublishingExtension::class) {
            publications {
                if (publicationName !in names) {
                    register(publicationName, MavenPublication::class) {
                        val component = components.findByName("java") ?: components["kotlin"]
                        from(component)
                    }
                }
            }
        }
    }
}
