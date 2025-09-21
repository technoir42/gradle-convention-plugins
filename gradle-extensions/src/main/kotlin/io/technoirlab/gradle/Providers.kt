package io.technoirlab.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.internal.extensions.core.extra

fun Project.localGradleProperty(propertyName: String): Provider<String> =
    provider { if (extra.has(propertyName)) extra.get(propertyName) as String? else null }
