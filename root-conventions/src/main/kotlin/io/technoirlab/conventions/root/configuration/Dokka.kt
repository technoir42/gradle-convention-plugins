package io.technoirlab.conventions.root.configuration

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension

internal fun Project.configureDokka() {
    pluginManager.apply("org.jetbrains.dokka")

    extensions.configure(DokkaExtension::class) {
        dokkaPublications.named("html") {
            outputDirectory.set(layout.projectDirectory.dir("docs"))
        }
    }
}
