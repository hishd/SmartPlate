package com.hishd.platekit.recognizer.util

fun String.isNumeric(): Boolean {
    return when(this.toIntOrNull()) {
        null -> false
        else -> true
    }
}