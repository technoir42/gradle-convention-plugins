package io.github.technoir42.conventions.common.fixtures

import org.intellij.lang.annotations.Language
import java.io.File

class GradleProject(
    val dir: File
) {
    fun project(name: String): GradleProject {
        require(":" !in name) { "Project name must not contain ':'" }
        return GradleProject(dir.resolve(name))
    }
}

val GradleProject.buildScript: File
    get() = dir.resolve("build.gradle.kts")

fun GradleProject.configureBuildScript(@Language("kotlin") code: String): GradleProject = apply {
    buildScript.appendText(code)
}
