package uk.co.conjure.screencapture

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.screenshot.Screenshot
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class ScreenCapture {
    companion object {

        var screenCaptureEnabled = false

        /**
         * @param name File name (optional, will be suffixed with a timestamp)
         * @param folder Folder name (optional, defaults to app name)
         * @param delay A delay in ms before screenshot is taken (default 200ms)
         */
        fun capture(name: String = "", folder: String = "", delay: Long = 200) {
            if (!screenCaptureEnabled) return
            try {

                if (delay > 0) {
                    Thread.sleep(delay)
                }

                val screenshot = Screenshot.capture()
                val fileName = StringBuilder().apply {
                    if (name.isNotBlank()) {
                        append(name)
                        append("_")
                    }
                    append(Date().time)
                    append(".webp")
                }.toString()

                val folderName = folder.ifEmpty {
                    InstrumentationRegistry.getInstrumentation().targetContext.let {
                        it.getString(it.applicationInfo.labelRes)
                    }
                }


                val fos = getFileOutputStream(fileName, folderName)

                val success = fos
                    .use {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            screenshot.bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 97, it)
                        } else {
                            screenshot.bitmap.compress(Bitmap.CompressFormat.WEBP, 97, it)
                        }
                    }

                if (success) {
                    println("Screenshot taken!")
                }
            } catch (e: Exception) {
                println("Failed saving screenshot: $e")
            }
        }

        private fun getFileOutputStream(fileName: String, folderName: String): OutputStream? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use MediaStore (no permission required)
                val resolver: ContentResolver =
                    InstrumentationRegistry.getInstrumentation().context.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/webp")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/" + folderName
                )
                val imageUri =
                    resolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )
                resolver.openOutputStream(imageUri!!)
            } else {
                // For Android 28 (PIE) access file directly (Permission required!)
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .toString()
                val image = File(imagesDir, fileName)
                FileOutputStream(image)
            }
        }
    }
}
