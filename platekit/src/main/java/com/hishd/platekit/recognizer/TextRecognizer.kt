package com.hishd.platekit.recognizer

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hishd.platekit.recognizer.variants.ThreeDigitPlateRecognizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object TextRecognizer {
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    /**
     * ### Usage
     * Start the recognition process using the provided [Uri]
     *
     * @param context the context which the process is being executed
     * @param uri the [Uri] of the source
     * @param callback the completion block which will deliver the Result
     */
    fun recognizeText(context: Context, uri: Uri, callback: (String?) -> Unit) {
        // [START run_detector]
        val image: InputImage = InputImage.fromFilePath(context, uri)
        textRecognizer.process(image)
            .addOnSuccessListener { visionText ->
                CoroutineScope(Dispatchers.IO).launch {
                    val result = ThreeDigitPlateRecognizer.recognize(visionText.text)
                    withContext(Dispatchers.Main) {
                        callback(result)
                    }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
        // [END run_detector]
    }

    /**
     * ### Usage
     * Start the recognition process using the provided [Bitmap]
     *
     * @param bitmap the [Bitmap] of the image source
     * @param callback the completion block which will deliver the Result
     */
    fun recognizeText(bitmap: Bitmap, callback: (String?) -> Unit) {
        // [START run_detector]
        val image: InputImage = InputImage.fromBitmap(bitmap, 0)
        textRecognizer.process(image)
            .addOnSuccessListener { visionText ->
                CoroutineScope(Dispatchers.Main).launch {
                    val result = ThreeDigitPlateRecognizer.recognize(visionText.text)
                    withContext(Dispatchers.Main) {
                        callback(result)
                    }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
        // [END run_detector]
    }

    //TODO: Remove the method after testing is done
    private suspend fun processTextBlock(result: Text): String? {
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
        return ThreeDigitPlateRecognizer.recognize(result.text)
        // [END mlkit_process_text_block]
    }
}