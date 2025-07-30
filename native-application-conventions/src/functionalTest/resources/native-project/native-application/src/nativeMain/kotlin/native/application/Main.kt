package native.application

import kotlinx.cinterop.ExperimentalForeignApi

@ExperimentalForeignApi
fun main() {
    nativeGreet("world")
}
