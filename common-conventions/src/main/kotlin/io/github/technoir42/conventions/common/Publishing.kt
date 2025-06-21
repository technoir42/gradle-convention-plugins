package io.github.technoir42.conventions.common

import org.gradle.api.Project
import org.gradle.api.attributes.Usage
import org.gradle.api.credentials.PasswordCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.credentials
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

fun Project.configurePublishing(isLibrary: Boolean = false) {
    pluginManager.apply("maven-publish")

    val projectDescription = provider { description }

    configure<PublishingExtension> {
        val publishRepositoryUrl = providers.gradleProperty("publishRepositoryUrl")
        repositories {
            maven(publishRepositoryUrl) {
                if (url.scheme != "file") {
                    credentials(PasswordCredentials::class)
                }
            }
        }

        publications {
            if (isLibrary) {
                register<MavenPublication>("libraryMaven") {
                    from(components["java"])
                }
            }

            withType<MavenPublication>().configureEach {
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

                if (name == "pluginMaven") {
                    suppressPomMetadataWarningsFor("apiElements")
                    suppressPomMetadataWarningsFor("runtimeElements")
                    suppressPomMetadataWarningsFor("apiApiElements")
                    suppressPomMetadataWarningsFor("apiRuntimeElements")
                    suppressPomMetadataWarningsFor("apiJavadocElements")
                    suppressPomMetadataWarningsFor("apiSourcesElements")
                }
            }
        }
    }
}
