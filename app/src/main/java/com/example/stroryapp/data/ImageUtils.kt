package com.example.stroryapp.data

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.example.stroryapp.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//object ImageUtils {
private const val MAXIMAL_SIZE = 1000000 //1 MB
//private lateinit var currentPhotoPath: String
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

//    fun allPermissionsGranted(context: Context, permission: String): Boolean {
//        return ContextCompat.checkSelfPermission(
//            context,
//            permission
//        ) == PackageManager.PERMISSION_GRANTED
//    }
fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: getImageUriForPreQ(context)
}
//    fun startGallery(launcherGallery: ActivityResultLauncher<PickVisualMediaRequest>) {
//        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//    }
//
//    fun startCamera(launcherIntentCamera: ActivityResultLauncher<Uri?>, context: Context): Uri {
//        val uri = getImageUri(context)
//        launcherIntentCamera.launch(uri)
//        return uri
//    }


    private fun getImageUriForPreQ(context: Context): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
        if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            imageFile
        )
    }
fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun Bitmap.getRotatedBitmap(file: File): Bitmap {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}

//    @Throws(IOException::class)
//    private fun createImageFile(context: Context): File {
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
//        val storageDir: File? = context.getExternalFilesDir(null)
//        return File.createTempFile(
//            "JPEG_${timeStamp}_",
//            ".jpg",
//            storageDir
//        ).apply {
//            currentPhotoPath = absolutePath
//        }
//    }
//
//    fun uriToFile(uri: Uri, context: Context): File {
//        val contentResolver = context.contentResolver
//        val myFile = createTempFile(context)
//
//        val inputStream = contentResolver.openInputStream(uri) ?: return myFile
//        val outputStream = myFile.outputStream()
//        inputStream.copyTo(outputStream)
//        inputStream.close()
//        outputStream.close()
//
//        return myFile
//    }
//
//    private fun createTempFile(context: Context): File {
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
//        val storageDir: File? = context.getExternalFilesDir(null)
//        return File.createTempFile(
//            "JPEG_${timeStamp}_",
//            ".jpg",
//            storageDir
//        )
//    }
//
//    fun File.reduceFileImage(): File {
//        // Implement image compression if necessary
//        return this
//    }
//
//    fun showImage(imageUri: Uri, imageView: ImageView) {
//        Log.d("Image URI", "showImage: $imageUri")
//        imageView.setImageURI(imageUri)
//    }
//}