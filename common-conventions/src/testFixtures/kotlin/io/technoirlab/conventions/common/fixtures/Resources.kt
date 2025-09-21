package io.technoirlab.conventions.common.fixtures

import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.copyToRecursively

@OptIn(ExperimentalPathApi::class)
fun copyResources(dirName: String, targetDir: Path) {
    val resourcesDir = Paths.get(::copyResources.javaClass.classLoader.getResource(dirName)!!.toURI())
    resourcesDir.copyToRecursively(targetDir, overwrite = false, followLinks = true)
}
