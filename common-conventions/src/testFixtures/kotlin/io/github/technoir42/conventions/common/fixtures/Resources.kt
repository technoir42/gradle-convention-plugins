package io.github.technoir42.conventions.common.fixtures

import java.io.File

fun copyResources(dirName: String, targetDir: File) {
    val resourcesDir = File(::copyResources.javaClass.classLoader.getResource(dirName)!!.toURI())
    resourcesDir.copyRecursively(targetDir)
}
