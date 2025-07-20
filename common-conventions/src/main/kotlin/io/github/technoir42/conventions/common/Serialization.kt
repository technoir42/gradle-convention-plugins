package io.github.technoir42.conventions.common

import org.gradle.api.Project
import org.gradle.api.provider.Provider

fun Project.configureKotlinSerialization(enable: Provider<Boolean>) {
    if (!enable.getOrElse(false)) return

    pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
}
