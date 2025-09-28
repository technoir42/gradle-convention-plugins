package io.technoirlab.conventions.root.configuration

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask

internal fun Project.configureDokka() {
    pluginManager.apply("org.jetbrains.dokka")

    extensions.configure(DokkaExtension::class) {
        dokkaPublications.named("html") {
            outputDirectory.set(layout.projectDirectory.dir("docs"))
        }
    }

    tasks.named(LifecycleBasePlugin.BUILD_TASK_NAME) {
        dependsOn(tasks.withType(DokkaGeneratePublicationTask::class))
    }
}
