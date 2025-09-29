package io.technoirlab.conventions.common.configuration

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure

fun Project.configureJava() {
    extensions.configure(JavaPluginExtension::class) {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(JDK_VERSION))
        }

        if ("java" in components.names) {
            withSourcesJar()
        }
    }
}

internal const val JDK_VERSION = 21
