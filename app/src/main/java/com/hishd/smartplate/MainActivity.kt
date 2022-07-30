package com.hishd.smartplate

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.hishd.platekit.recognizer.TextRecognizer
import com.hishd.smartplate.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val isCameraPermissionsGranted: Boolean get() = checkAndRequestPermissions()

    private var tempImageUri: Uri? = null
    private lateinit var resultLauncherCamera: ActivityResultLauncher<Uri>
    private lateinit var resultLauncherGallery: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAndRequestPermissions()
        initiateResultLaunchers()
        setActions()
    }

    private fun initiateResultLaunchers() {
        resultLauncherCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            binding.imgSelected.apply {
                setImageURI(null)
                setImageURI(tempImageUri)
            }
            recognizeText(tempImageUri)
        }

        resultLauncherGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.imgSelected.apply {
                setImageURI(null)
                setImageURI(uri)
            }
            recognizeText(uri)
        }
    }

    private fun setActions() {
        binding.btnCamera.setOnClickListener { view ->
            if(isCameraPermissionsGranted) {
                captureFromCamera()
            }
        }
        binding.btnGallery.setOnClickListener { view ->
            captureFromGallery()
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

    private fun captureFromGallery() = resultLauncherGallery.launch("image/*")

    private fun captureFromCamera() {
        if(tempImageUri == null) {
            tempImageUri = initTempImageUri()
        }
        resultLauncherCamera.launch(tempImageUri)
    }

    private fun initTempImageUri(): Uri {
        val tempImagesDir = File(applicationContext.filesDir, "temp_images")
        tempImagesDir.mkdir()
        val tempImage = File(tempImagesDir, "temp_image.jpg")
        return FileProvider.getUriForFile(applicationContext, applicationContext.packageName, tempImage)
    }

    private fun recognizeText(imageUri: Uri?) {
        binding.lblResult.text = "Processing...."
        if(imageUri == null) {
            Toast.makeText(this@MainActivity, "Provided image Uri is null", Toast.LENGTH_SHORT).show()
            return
        }
        TextRecognizer.recognizeText(this@MainActivity, imageUri) { result ->
            binding.lblResult.text = result
        }
    }

}