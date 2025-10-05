package io.technoirlab.conventions.gradle.plugin.configuration

import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

internal val GradleVersion.kotlinLanguageVersion: KotlinVersion
    get() = when {
        this >= GradleVersion.version("9.0") -> KotlinVersion.KOTLIN_2_2
        this >= GradleVersion.version("8.11") -> KotlinVersion.KOTLIN_1_8
        else -> error("Gradle version $this is unsupported")
    }

internal val GradleVersion.embeddedKotlinVersion: String
    get() = when {
        this >= GradleVersion.version("9.2") -> "2.2.20"
        this >= GradleVersion.version("9.0") -> "2.2.0"
        this >= GradleVersion.version("8.12") -> "2.0.21"
        this >= GradleVersion.version("8.11") -> "2.0.20"
        else -> error("Gradle version $this is unsupported")
    }
