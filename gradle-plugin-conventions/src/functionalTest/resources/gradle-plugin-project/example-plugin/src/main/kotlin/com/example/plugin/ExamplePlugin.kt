package com.example.plugin

import com.example.plugin.api.ExampleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

/**
 * Example Gradle plugin.
 */
class ExamplePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create<ExampleExtension>(ExampleExtension.NAME)
        // body placeholder
    }

    // function placeholder
}
