package dev.efantini.photoforsetest.utils

open class Event<out T>(
    private val content: T,
) {
    private var consumed = false

    fun consume(): T? =
        if (consumed) {
            null
        } else {
            consumed = true
            content
        }
}
