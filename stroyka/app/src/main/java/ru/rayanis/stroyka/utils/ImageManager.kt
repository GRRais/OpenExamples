package ru.rayanis.stroyka.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

object ImageManager {

    private const val MAX_IMAGE_SIZE = 1000
    private const val WIDTH = 0
    private const val HEIGHT = 1

    //получаем размеры изображения
    fun getImageSize(uri: Uri, act: Activity): List<Int>{
        val inStream = act .contentResolver.openInputStream(uri)
//        val fTemp = File(act.cacheDir, "temp.tmp")
//        if (inStream != null) {
//            fTemp.copyInStreamToFile(inStream)
//        }
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(inStream, null, options)
        return listOf(options.outWidth, options.outHeight)

    }

    //копируем изображение из потока в файл
    private fun File.copyInStreamToFile(inStream: InputStream) {
        this.outputStream().use {
            out -> inStream.copyTo(out)
        }
    }

    //получаем тип файла, портретный или альбомный
    fun chooseScaleType(im: ImageView, bitmap: Bitmap) {
        if (bitmap.width > bitmap.height) {
            im.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            im.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
    }

    //сжимаем изображение, если больше ширина или высота больше MAX_IMAGE_SIZE
    suspend fun imageResize(uris: ArrayList<Uri>, act: Activity): List<Bitmap> = withContext(Dispatchers.IO) {
        val tempList = ArrayList<List<Int>>()
        val bitmapList = ArrayList<Bitmap>()
        for (n in uris.indices){
            val size = getImageSize(uris[n], act)
            val imageRatio = size[WIDTH].toFloat() / size[HEIGHT].toFloat()

            if (imageRatio > 1){
                if (size[WIDTH] > MAX_IMAGE_SIZE){
                    tempList.add(listOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE/imageRatio).toInt()))
                } else {
                    tempList.add(listOf(size[WIDTH], size[HEIGHT]))
                }
            } else {
                if (size[HEIGHT] > MAX_IMAGE_SIZE){
                    tempList.add(listOf((MAX_IMAGE_SIZE*imageRatio).toInt() , MAX_IMAGE_SIZE))
                } else {
                    tempList.add(listOf(size[WIDTH], size[HEIGHT]))
                }
            }
        }

        for (i in uris.indices) {
            kotlin.runCatching {
                bitmapList.add(
                    Picasso.get().load(uris[i]).resize(tempList[i][WIDTH], tempList[i][HEIGHT])
                        .get())
            }
        }
        return@withContext bitmapList
    }

    suspend fun getBitmapFromUris(uris: List<String?>): List<Bitmap> = withContext(Dispatchers.IO) {
        val bitmapList = ArrayList<Bitmap>()
        for (i in uris.indices) {
            kotlin.runCatching {
                bitmapList.add(
                    Picasso.get().load(uris[i]).get())
            }
        }
        return@withContext bitmapList
    }
}