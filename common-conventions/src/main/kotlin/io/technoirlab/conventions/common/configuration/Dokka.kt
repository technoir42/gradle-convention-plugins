package io.technoirlab.conventions.common.configuration

import io.technoirlab.gradle.capitalized
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.jetbrains.dokka.gradle.DokkaExtension

fun Project.configureDokka(docsFormats: Set<DocsFormat> = DocsFormat.Multiplatform) {
    pluginManager.apply("org.jetbrains.dokka")
    if (DocsFormat.Javadoc in docsFormats) {
        pluginManager.apply("org.jetbrains.dokka-javadoc")
    }

    extensions.configure(DokkaExtension::class) {
        dokkaPublications.configureEach {
            val docsFormat = DocsFormat.entries.first { it.name.lowercase() == name }
            val generateTask = tasks.named("dokkaGeneratePublication${formatName.capitalized()}")
            tasks.register<Jar>("dokka${formatName.capitalized()}Jar") {
                from(outputDirectory)
                dependsOn(generateTask)
                archiveClassifier.set(docsFormat.classifier)
                destinationDirectory.set(layout.buildDirectory.dir("dokka"))
            }
        }
    }
}

internal fun Project.dokkaJar(format: DocsFormat): TaskProvider<Jar> =
    tasks.named<Jar>("dokka${format.name.capitalized()}Jar")
