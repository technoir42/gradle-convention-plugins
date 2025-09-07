package io.github.technoir42.conventions.common

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure

fun Project.configureJava() {
    extensions.configure(JavaPluginExtension::class) {
        toolchain {
            @Suppress("MagicNumber")
            languageVersion.set(JavaLanguageVersion.of(21))
        }

        if ("java" in components.names) {
            withSourcesJar()
        }
    }
}
