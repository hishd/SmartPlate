package com.hishd.smartplate

import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

object TextRecognizer {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun recognizeText(image: InputImage, callback: (String) -> Unit) {
        // [START run_detector]
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                callback(processTextBlock(visionText))
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
        // [END run_detector]
    }

    private fun processTextBlock(result: Text): String {
        // [START mlkit_process_text_block]
//        val resultText = result.text
//        for (block in result.textBlocks) {
//            val blockText = block.text
//            val blockCornerPoints = block.cornerPoints
//            val blockFrame = block.boundingBox
//            for (line in block.lines) {
//                val lineText = line.text
//                val lineCornerPoints = line.cornerPoints
//                val lineFrame = line.boundingBox
//                for (element in line.elements) {
//                    val elementText = element.text
//                    val elementCornerPoints = element.cornerPoints
//                    val elementFrame = element.boundingBox
//                }
//            }
//        }
        return result.text
        // [END mlkit_process_text_block]
    }
}