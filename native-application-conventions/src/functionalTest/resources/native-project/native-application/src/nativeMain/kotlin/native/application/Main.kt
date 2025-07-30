package native.application

import kotlinx.cinterop.ExperimentalForeignApi
import lib.nativeGreet

@ExperimentalForeignApi
fun main() {
    nativeGreet("world")
}
