package com.hishd.smartplate

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hishd.smartplate.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private lateinit var binding: ActivityMainBinding
    private val isCameraPermissionsGranted: Boolean get() = checkAndRequestPermissions()

    private var image_uri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    private val IMAGE_CAPTURE_CODE = 600

    private var tempImageUri: Uri? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAndRequestPermissions()
        initiateResultLaunchers()
        setActions()
    }

    private fun initiateResultLaunchers() {
        resultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            binding.imgSelected.setImageURI(null)
            binding.imgSelected.setImageURI(tempImageUri)
        }
    }

    private fun setActions() {
        binding.btnCamera.setOnClickListener { view ->
            if(isCameraPermissionsGranted) {
                captureFromCamera()
            }
        }
        binding.btnGallery.setOnClickListener { view ->

        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        return if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED) {
            val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission, 112)
            Toast.makeText(this, "Please provide camera permissions", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun captureFromCamera() {
//        val values = ContentValues()
//        values.put(MediaStore.Images.Media.TITLE, "Captured Image")
//        values.put(MediaStore.Images.Media.DESCRIPTION, "Captured Image from camera")
//        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)

        if(tempImageUri == null) {
            tempImageUri = initTempImageUri()
        }
        resultLauncher.launch(tempImageUri)
    }

    private fun initTempImageUri(): Uri {
        val tempImagesDir = File(applicationContext.filesDir, "temp_images")
        tempImagesDir.mkdir()
        val tempImage = File(tempImagesDir, "temp_image.jpg")
        return FileProvider.getUriForFile(applicationContext, applicationContext.packageName, tempImage)
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