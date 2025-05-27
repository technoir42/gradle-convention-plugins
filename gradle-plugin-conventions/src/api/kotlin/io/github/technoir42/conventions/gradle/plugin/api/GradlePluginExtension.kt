package io.github.technoir42.conventions.gradle.plugin.api

import io.github.technoir42.conventions.common.api.CommonExtension
import org.gradle.api.Action
import org.gradle.api.tasks.Nested

@GradlePluginDsl
interface GradlePluginExtension : CommonExtension {
    /**
     * Optional build features.
     */
    @get:Nested
    override val buildFeatures: GradlePluginBuildFeatures

    fun buildFeatures(action: Action<GradlePluginBuildFeatures>) {
        action.execute(buildFeatures)
    }

    companion object {
        const val NAME = "gradlePluginConfig"
    }
}
