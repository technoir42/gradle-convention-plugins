package io.github.technoir42.conventions.common.fixtures

import java.io.File

fun File.resolve(vararg segments: String): File =
    segments.fold(this) { current, segment -> File(current, segment) }

fun File.replaceText(oldText: String, newText: String) {
    require(oldText != newText) { "oldText and newText must be different" }

    val content = readText()
    val newContent = content.replaceFirst(oldText, newText)
    check(content != newContent) { "Text '$oldText' not found in file '$path'" }
    writeText(newContent)
}
