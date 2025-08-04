package io.github.technoir42.conventions.jvm.api

import io.github.technoir42.conventions.common.api.CommonExtension
import org.gradle.api.Action
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

@JvmApplicationDsl
interface JvmApplicationExtension : CommonExtension {
    /**
     * The name of the application's main class.
     * Can be a fully qualified name or relative to the package name, e.g. `com.example.MainKt` or `.MainKt`.
     */
    val mainClass: Property<String>

    /**
     * The list of string arguments to pass to the JVM when running the application.
     */
    val jvmArgs: ListProperty<String>

    /**
     * Optional build features.
     */
    @get:Nested
    override val buildFeatures: JvmBuildFeatures

    fun buildFeatures(action: Action<JvmBuildFeatures>) {
        action.execute(buildFeatures)
    }

    companion object {
        const val NAME = "jvmApplication"
    }
}
