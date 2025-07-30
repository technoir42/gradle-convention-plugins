package io.github.technoir42.conventions.common.api

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

@CommonDsl
interface CommonExtension {
    /**
     * The base package name.
     * Defaults to sanitised project name, e.g. `jvm-library` -> `jvm.library`.
     */
    val packageName: Property<String>

    /**
     * Optional build features.
     */
    @get:Nested
    val buildFeatures: CommonBuildFeatures

    fun initDefaults(projectName: String) {
        packageName.convention(projectName.replace('-', '.').replace('_', '.'))
        buildFeatures.initDefaults()
    }
}
