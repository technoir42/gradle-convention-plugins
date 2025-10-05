package io.technoirlab.conventions.gradle.plugin.api

import io.technoirlab.conventions.common.api.CommonExtension
import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

@GradlePluginDsl
interface GradlePluginExtension : CommonExtension {
    /**
     * Minimum Gradle version supported by the plugin. Defaults to `9.0`.
     */
    val minGradleVersion: Property<String>

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

    override fun initDefaults(projectName: String) {
        super.initDefaults(projectName)
        minGradleVersion.convention("9.0")
    }

    /**
     * @suppress
     */
    companion object {
        const val NAME = "gradlePluginConfig"
    }
}
