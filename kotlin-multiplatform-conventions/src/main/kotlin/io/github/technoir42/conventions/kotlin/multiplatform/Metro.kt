package io.github.technoir42.conventions.kotlin.multiplatform

import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal fun Project.configureMetro(enableMetro: Provider<Boolean>) {
    if (!enableMetro.get()) return

    pluginManager.apply("dev.zacsweers.metro")
}
