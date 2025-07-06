package io.github.technoir42.conventions.common.api

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

@CommonDsl
interface CommonExtension {
    /**
     * The base package name.
     */
    val packageName: Property<String>

    /**
     * Optional build features.
     */
    @get:Nested
    val buildFeatures: CommonBuildFeatures
}
