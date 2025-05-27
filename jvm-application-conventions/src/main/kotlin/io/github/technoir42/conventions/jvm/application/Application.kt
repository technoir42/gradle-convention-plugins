package io.github.technoir42.conventions.jvm.application

import io.github.technoir42.conventions.jvm.application.api.JvmApplicationExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.gradle.kotlin.dsl.configure

internal fun Project.configureApplication(config: JvmApplicationExtension) {
    configure<JavaApplication> {
        mainClass.set(config.mainClass)

        afterEvaluate {
            applicationDefaultJvmArgs = config.jvmArgs.get()
        }
    }
}
