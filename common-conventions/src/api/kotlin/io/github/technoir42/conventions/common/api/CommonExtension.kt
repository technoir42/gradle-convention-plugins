package io.github.technoir42.conventions.common.api

import org.gradle.api.tasks.Nested

@CommonDsl
interface CommonExtension {
    /**
     * Optional build features.
     */
    @get:Nested
    val buildFeatures: CommonBuildFeatures
}
