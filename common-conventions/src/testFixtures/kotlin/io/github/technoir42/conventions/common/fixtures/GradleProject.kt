package io.github.technoir42.conventions.common.fixtures

import org.intellij.lang.annotations.Language
import java.nio.file.Path
import kotlin.io.path.appendText
import kotlin.io.path.div

class GradleProject(
    val dir: Path
) {
    fun project(name: String): GradleProject {
        require(":" !in name) { "Project name must not contain ':'" }
        return GradleProject(dir / name)
    }
}

val GradleProject.buildScript: Path
    get() = dir / "build.gradle.kts"

fun GradleProject.kotlinFile(className: String, variant: String = "main") =
    dir / "src/$variant/kotlin/${className.replace('.', '/')}.kt"

fun GradleProject.configureBuildScript(@Language("kotlin") code: String): GradleProject =
    apply { buildScript.appendText(code) }
