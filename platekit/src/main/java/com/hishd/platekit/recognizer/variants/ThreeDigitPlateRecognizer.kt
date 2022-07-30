package com.hishd.platekit.recognizer.variants

import android.util.Log
import com.hishd.platekit.recognizer.util.isNumeric
import kotlinx.coroutines.coroutineScope
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ThreeDigitPlateRecognizer {
    private const val TAG: String = "ThreeDigitPlateRecognizer"
    private var prefixDigits: String? = null
    private var suffixDigits: String? = null

    private var isPrefixCaptured: Boolean = false
    private var isSuffixCaptured: Boolean = false

    suspend fun recognize(input: String): String? = suspendCoroutine { continuation ->
        //Resetting Values
        prefixDigits = null
        suffixDigits = null
        isPrefixCaptured = false
        isSuffixCaptured = false

        Log.d(TAG, input)
        //Step 01: Add [brkln] objects to the line breaks and spaces
        val lines = input.replace("\n", "[brkln]").replace(" ", "[brkln]").lowercase()

        //Step 02: Separate the groups using [NWLN] object
        val splitGroup = lines.split("[brkln]")

        splitGroup.forEach { text ->
            if(!isPrefixCaptured) {
                capturePrefix(text)
            } else {
                captureSuffix(text)
            }
        }

        if(prefixDigits != null && suffixDigits != null) {
            continuation.resume(String.format(Locale.ENGLISH, "%s-%s", prefixDigits!!.uppercase(), suffixDigits!!.uppercase()))
        } else {
            continuation.resume(null)
        }
    }

    private fun capturePrefix(input: String) {
        Log.d(TAG, "Checking Prefix : $input")
        if(isPrefixCaptured)
            return

        if(input.length < 3) {
            Log.d(TAG, "Input is less than 3 digits, Skipping Prefix Check....!")
            return
        }

        if(input.length == 3) {
            val firstChar = input.first()
            if(firstChar == 'c' || firstChar == 'b' || firstChar == 'a') {
                isPrefixCaptured = true
                prefixDigits = input
                Log.d(TAG, "Prefix is : $prefixDigits")
            } else {
                return
            }
        } else {
            capturePrefix(input.drop(1))
        }
    }

    private fun captureSuffix(input: String) {
        Log.d(TAG, "Checking Suffix : $input")
        if(isSuffixCaptured)
            return

        if(!input.isNumeric()) {
            return
        }

        if(input.length < 4) {
            Log.d(TAG, "Input is less than 4 digits, Skipping Suffix Check....!")
            return
        }

        if(input.length == 4) {
            isSuffixCaptured = true
            suffixDigits = input
            Log.d(TAG, "Suffix is : $suffixDigits")
        } else {
            captureSuffix(input.drop(1))
        }
    }
}