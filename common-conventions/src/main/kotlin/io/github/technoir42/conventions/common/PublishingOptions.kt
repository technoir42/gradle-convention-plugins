package io.github.technoir42.conventions.common

data class PublishingOptions(
    val componentName: String,
    val publicationName: String,
    val docsFormats: Set<DocsFormat> = DocsFormat.Multiplatform
)
