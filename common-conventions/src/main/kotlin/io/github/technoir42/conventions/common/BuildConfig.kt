package io.github.technoir42.conventions.common

import com.github.gmazzo.buildconfig.BuildConfigExtension
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure

fun Project.configureBuildConfig(enable: Provider<Boolean>, packageName: Property<String>) {
    if (!enable.get()) return

    pluginManager.apply("com.github.gmazzo.buildconfig")

    configure<BuildConfigExtension> {
        this.packageName.set(packageName)
    }
}
