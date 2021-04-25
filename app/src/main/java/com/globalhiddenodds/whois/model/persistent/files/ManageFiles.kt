package com.globalhiddenodds.whois.model.persistent.files

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.DisplayMetrics
import com.globalhiddenodds.whois.R
import org.jetbrains.anko.displayMetrics
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageFiles @Inject constructor(private val context: Context) {
    private val directoryWork = "whois"
    private var displayMetrics: DisplayMetrics? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private val imageFile = "temp.jpg"
    private val quality = 100

    init {
        this.displayMetrics = context.displayMetrics
        this.screenWidth = displayMetrics!!.widthPixels
        this.screenHeight = displayMetrics!!.heightPixels
    }

    private fun getStorageDirectory(): File {
        val dir = File(context.filesDir, directoryWork)
        return if (!dir.exists()){
            dir.mkdirs()
            dir
        }
        else{
            dir
        }
    }

    fun getBitmap(uri: Uri?): Bitmap? {
        when {
            uri != null -> return try {
                val source = ImageDecoder
                    .createSource(context.contentResolver, uri)
                scaleBitmapDown(ImageDecoder.decodeBitmap(source))

            } catch (e: IOException) {
                println(e.message)
                null
            }
            else -> {
                println(R.string.value_null)
                return null
            }

        }
    }

    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, file: String) {
        val pathImage = File(getStorageDirectory(), file)
        val stream = FileOutputStream(pathImage)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()
    }

    private fun scaleBitmapDown(bitmap: Bitmap): Bitmap {
        return if (this.screenWidth < bitmap.width){
            val diffRelationSize = this.screenWidth.toFloat() / bitmap.width.toFloat()
            Bitmap.createScaledBitmap(bitmap,
                (bitmap.width * diffRelationSize).toInt(),
                (bitmap.height * diffRelationSize).toInt(), true)
        }else{
            bitmap
        }

    }

    fun isFileExist(file: String): Boolean{
        val fileFind = File(getStorageDirectory(), file)
        if (fileFind.exists()){
            return true
        }
        return false
    }

    private fun deleteImagesJpeg(){
        val dir = getStorageDirectory()
        val files = dir.listFiles()
        files?.asSequence()?.filter {
            (it.endsWith(".jpg") || it.endsWith(".jpeg"))
                    && it.delete() }?.forEach { _ -> println("Delete OK") }

    }

    fun deleteFile(fileName: String){
        try {
            val file = File(getStorageDirectory(), fileName)
            file.delete()
        } catch (e: FileNotFoundException) {
            println("File not found: $e")
        } catch (e: IOException) {
            println("Can not read file: $e")
        }

    }

    fun getCameraFile(): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(dir, imageFile)
    }

    fun readFileDirectlyAsText(file: String): String = File(getStorageDirectory(), file)
        .readText(Charsets.UTF_8)

    fun writeFileDirectly(file: String, fileContent: String) =
        File(getStorageDirectory(), file).writeText(fileContent + "\n")

    fun base641EncodedImage(bitmap: Bitmap): String{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    fun base64DecodeImage(baseString: String): Bitmap {
        val decode: ByteArray = Base64.decode(baseString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decode,
            0, decode.size)
    }
}