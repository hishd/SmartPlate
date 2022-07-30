package com.hishd.smartplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hishd.smartplate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun recognizeText(image: InputImage) {
        // [START run_detector]
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                processTextBlock(visionText)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
        // [END run_detector]
    }

    private fun processTextBlock(result: Text) {
        // [START mlkit_process_text_block]
        val resultText = result.text
        for (block in result.textBlocks) {
            val blockText = block.text
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox
            for (line in block.lines) {
                val lineText = line.text
                val lineCornerPoints = line.cornerPoints
                val lineFrame = line.boundingBox
                for (element in line.elements) {
                    val elementText = element.text
                    val elementCornerPoints = element.cornerPoints
                    val elementFrame = element.boundingBox
                }
            }
        }
        // [END mlkit_process_text_block]
    }
}