package io.github.technoir42.conventions.common

import org.gradle.api.Project
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.base.TestingExtension

fun Project.configureTesting() {
    pluginManager.apply("jvm-test-suite")

    @Suppress("UnstableApiUsage")
    configure<TestingExtension> {
        suites.withType<JvmTestSuite>().configureEach {
            useJUnitJupiter(DependencyVersions.JUNIT)
        }
    }
}
