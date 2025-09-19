package io.github.technoir42.conventions.common

import io.github.technoir42.gradle.Environment
import org.gradle.api.Project
import org.gradle.api.attributes.Usage
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension

fun Project.configurePublishing(
    publicationName: String,
    environment: Environment,
    extraConfiguration: MavenPublication.() -> Unit = {}
) {
    pluginManager.apply("maven-publish")
    pluginManager.apply("signing")

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

        publications.withType<MavenPublication>().configureEach {
            pom {
                url.convention(environment.repositoryUrl.map { it.toString() })
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
        val publishing = extensions.getByType<PublishingExtension>()
        publishing.run {
            publications {
                if (publicationName !in names) {
                    register(publicationName, MavenPublication::class) {
                        val component = components.findByName("java") ?: components["kotlin"]
                        from(component)
                    }
                }
            }
        }

        extensions.configure(SigningExtension::class) {
            val secretKey = providers.environmentVariable("SIGNING_KEY")
            val password = providers.environmentVariable("SIGNING_PASSWORD")
            useInMemoryPgpKeys(secretKey.orNull, password.orNull)
            sign(publishing.publications)
            isRequired = false
        }
    }
}
