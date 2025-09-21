package io.technoirlab.conventions.common.configuration

import io.technoirlab.conventions.common.BuildConfig
import org.gradle.api.Project
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.base.TestingExtension

fun Project.configureTesting() {
    pluginManager.apply("jvm-test-suite")

    @Suppress("UnstableApiUsage")
    extensions.configure(TestingExtension::class) {
        suites.withType<JvmTestSuite>().configureEach {
            useJUnitJupiter(BuildConfig.JUNIT5_VERSION)
        }
    }
}
