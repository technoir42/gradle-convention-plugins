package io.technoirlab.conventions.common.configuration

import io.technoirlab.gradle.Environment
import io.technoirlab.gradle.capitalized
import io.technoirlab.gradle.copy
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask
import java.io.File
import java.net.URI

fun Project.configureDokka(environment: Environment, docsFormats: Set<DocsFormat> = DocsFormat.Multiplatform) {
    pluginManager.apply("org.jetbrains.dokka")
    if (DocsFormat.Javadoc in docsFormats) {
        pluginManager.apply("org.jetbrains.dokka-javadoc")
    }

    extensions.configure(DokkaExtension::class) {
        dokkaPublications.configureEach {
            suppressInheritedMembers.set(true)

            val docsFormat = DocsFormat.entries.first { it.name.lowercase() == name }
            val generateTask = tasks.named("dokkaGeneratePublication${formatName.capitalized()}")
            tasks.register<Jar>("dokka${formatName.capitalized()}Jar") {
                from(outputDirectory)
                dependsOn(generateTask)
                archiveClassifier.set(docsFormat.classifier)
                destinationDirectory.set(layout.buildDirectory.dir("dokka"))
            }
        }

        val srcDir = layout.projectDirectory.dir("src")
        val sourceUrl = environment.getSourceUrl(srcDir, layout.settingsDirectory)
        dokkaSourceSets.configureEach {
            jdkVersion.set(JDK_VERSION)

            externalDocumentationLinks {
                register("gradle-api") {
                    url("https://docs.gradle.org/${gradle.gradleVersion}/javadoc/")
                    packageListUrl("https://docs.gradle.org/${gradle.gradleVersion}/javadoc/element-list")
                }

                register("kotlinx-coroutines") {
                    url("https://kotlinlang.org/api/kotlinx.coroutines/")
                }

                register("kotlinx-datetime") {
                    url("https://kotlinlang.org/api/kotlinx-datetime/")
                    packageListUrl("https://kotlinlang.org/api/kotlinx-datetime/kotlinx-datetime/package-list")
                }

                register("kotlinx-io") {
                    url("https://kotlinlang.org/api/kotlinx-io/")
                }

                register("kotlinx-serialization") {
                    url("https://kotlinlang.org/api/kotlinx.serialization/")
                }
            }

            if (sourceUrl.isPresent) {
                sourceLink {
                    localDirectory.set(srcDir)
                    remoteUrl.set(sourceUrl)
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }

    tasks.named(LifecycleBasePlugin.BUILD_TASK_NAME) {
        dependsOn(tasks.withType(DokkaGeneratePublicationTask::class))
    }
}

internal fun Project.dokkaJar(format: DocsFormat): TaskProvider<Jar> =
    tasks.named<Jar>("dokka${format.name.capitalized()}Jar")

private fun Environment.getSourceUrl(srcDir: Directory, rootDir: Directory): Provider<URI> {
    var relativePath = srcDir.asFile.toRelativeString(rootDir.asFile)
    if (File.separatorChar != '/') {
        relativePath = relativePath.replace(File.separatorChar, '/')
    }
    return repositoryUrl.zip(branchName) { repoUrl, branchName ->
        val baseUrl = if (repoUrl.path.endsWith("/")) repoUrl else repoUrl.copy(path = repoUrl.path + "/")
        baseUrl.resolve("tree/$branchName/$relativePath")
    }
}
