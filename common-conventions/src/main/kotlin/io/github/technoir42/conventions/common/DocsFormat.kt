package io.github.technoir42.conventions.common

enum class DocsFormat(val classifier: String) {
    Html("html-docs"),
    Javadoc("javadoc");

    companion object {
        val All = setOf(Html, Javadoc)
        val Multiplatform = setOf(Html)
    }
}
