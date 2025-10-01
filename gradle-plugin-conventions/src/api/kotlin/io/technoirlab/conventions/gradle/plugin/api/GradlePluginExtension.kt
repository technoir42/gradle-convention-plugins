package io.technoirlab.conventions.gradle.plugin.api

import io.technoirlab.conventions.common.api.CommonExtension
import org.gradle.api.Action
import org.gradle.api.tasks.Nested

@GradlePluginDsl
interface GradlePluginExtension : CommonExtension {
    /**
     * Optional build features.
     */
    @get:Nested
    override val buildFeatures: GradlePluginBuildFeatures

    /**
     * Optional build features.
     */
    fun buildFeatures(action: Action<GradlePluginBuildFeatures>) {
        action.execute(buildFeatures)
    }

    /**
     * @suppress
     */
    companion object {
        const val NAME = "gradlePluginConfig"
    }
}
