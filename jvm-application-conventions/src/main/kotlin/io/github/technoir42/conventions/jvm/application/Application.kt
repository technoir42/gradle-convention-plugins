package io.github.technoir42.conventions.jvm.application

import io.github.technoir42.conventions.jvm.application.api.JvmApplicationExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure

internal fun Project.configureApplication(config: JvmApplicationExtension) {
    configure<JavaApplication> {
        mainClass.set(config.fullyQualifiedMainClass)

        afterEvaluate {
            applicationDefaultJvmArgs = config.jvmArgs.get()
        }
    }
}

private val JvmApplicationExtension.fullyQualifiedMainClass: Provider<String>
    get() = packageName.zip(mainClass) { packageName, mainClass ->
        if (mainClass.startsWith(".")) {
            "$packageName$mainClass"
        } else {
            mainClass
        }
    }
        .orElse(mainClass)
