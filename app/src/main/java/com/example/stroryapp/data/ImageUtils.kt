package com.example.stroryapp.data

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.stroryapp.BuildConfig
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageUtils {
    private lateinit var currentPhotoPath: String

    fun allPermissionsGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun startGallery(launcherGallery: ActivityResultLauncher<PickVisualMediaRequest>) {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun startCamera(launcherIntentCamera: ActivityResultLauncher<Uri?>, context: Context): Uri {
        val uri = getImageUri(context)
        launcherIntentCamera.launch(uri)
        return uri
    }

    private fun getImageUri(context: Context): Uri {
        val photoFile: File = createImageFile(context)
        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            photoFile
        )
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = context.getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    fun uriToFile(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val myFile = createTempFile(context)

        val inputStream = contentResolver.openInputStream(uri) ?: return myFile
        val outputStream = myFile.outputStream()
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()

        return myFile
    }

    private fun createTempFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = context.getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    fun File.reduceFileImage(): File {
        // Implement image compression if necessary
        return this
    }

    fun showImage(imageUri: Uri, imageView: ImageView) {
        Log.d("Image URI", "showImage: $imageUri")
        imageView.setImageURI(imageUri)
    }
}