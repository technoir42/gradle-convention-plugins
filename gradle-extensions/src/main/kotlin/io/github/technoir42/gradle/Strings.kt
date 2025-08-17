package io.github.technoir42.gradle

fun String.capitalized(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
