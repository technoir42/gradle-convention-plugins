package io.github.technoir42.conventions.common

import org.gradle.api.Project
import org.gradle.api.provider.Provider

fun Project.configureKotlinSerialization(enable: Provider<Boolean>) {
    afterEvaluate {
        if (enable.getOrElse(false)) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        }
    }
}
