package com.attri.swissarmy.feature.imagetools.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageToolsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun compressImage(uri: Uri, quality: Int): File = withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        
        val outputFile = File(context.cacheDir, "compressed_image_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(outputFile)
        
        try {
            // Compress using JPEG
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            
            // Check if we need to recycle
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
            return@withContext outputFile
        } finally {
            outputStream.flush()
            outputStream.close()
            inputStream?.close()
        }
    }
}
