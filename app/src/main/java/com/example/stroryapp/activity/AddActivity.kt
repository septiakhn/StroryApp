package com.example.stroryapp.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.stroryapp.R
import com.example.stroryapp.activity.LoginActivity
import com.example.stroryapp.activity.MainActivity
import com.example.stroryapp.data.Result
import com.example.stroryapp.data.getImageUri
import com.example.stroryapp.data.reduceFileImage
import com.example.stroryapp.data.uriToFile
import com.example.stroryapp.databinding.ActivityAddBinding
import com.example.stroryapp.viewModel.AddViewModel
import com.example.stroryapp.viewModel.ViewModelFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<AddViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else {

                binding.btUpload.setOnClickListener {
                    uploadStory(user.token)
                }
            }
        }
        binding.btCamera.setOnClickListener {
            startCamera()
        }
        binding.btGaleri.setOnClickListener {
            startGallery()
        }
//        binding.btCamera.setOnClickListener {
//            if (ImageUtils.allPermissionsGranted(this, REQUIRED_PERMISSION)) {
//                currentImageUri = ImageUtils.startCamera(launcherIntentCamera, this)
//            }
//        }
//
//        binding.btGaleri.setOnClickListener {
//            ImageUtils.startGallery(launcherGallery)
//        }
    }
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun startCamera() {
//        currentImageUri = getImageUri(this)
//        launcherIntentCamera.launch(currentImageUri)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            currentImageUri = getImageUri(this)
            launcherIntentCamera.launch(currentImageUri)
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                showToast("Camera permission is required to take photos")
            }
        }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imageView2.setImageURI(it)
        }
    }
    private fun uploadStory(token: String) {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.deskripsi.text.toString()

            viewModel.uploadImage(token, imageFile, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            // Show loading indicator if needed
                        }
                        is Result.Success -> {
                            result.data.message.let { showToast(it!!) }

                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        is Result.Error -> {
                            showToast(result.error)
                        }
                    }
                }
            }
        } ?: showToast(getString(R.string.image_warning))
    }

//    private fun uploadStory(token: String, lat: String, lon: String) {
//
//        currentImageUri?.let { uri ->
//
//            val imageFile = uriToFile(uri, this).reduceFileImage()
//            Log.d("Image File", "showImage: ${imageFile.path}")
//            val description = binding.deskripsi.text.toString()
//
//
//            viewModel.uploadImage(
//                token,
//                imageFile,
//                description,
//                lat, lon
//            ).observe(this) { result ->
//                if (result != null) {
//                    when (result) {
//                        is Result.Loading -> {
//
//                        }
//
//                        is Result.Success -> {
//                            result.data.message.let { showToast(it!!) }
//                            val intent = Intent(this, MainActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            startActivity(intent)
//                            finish()
//                        }
//
//                        is Result.Error -> {
//                            showToast(result.error)
//                            // showLoading(false)
//                        }
//                    }
//                }
//            }
//
//        } ?: showToast(getString(R.string.image_warning))
//    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
