package com.hishd.platekit.recognizer.util

/**
 * An utility method to check a string is numeric using [String] extensions
 */
fun String.isNumeric(): Boolean {
    return when(this.toIntOrNull()) {
        null -> false
        else -> true
    }
}