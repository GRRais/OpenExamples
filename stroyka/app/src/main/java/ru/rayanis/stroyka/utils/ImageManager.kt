package ru.rayanis.stroyka.utils

import android.graphics.BitmapFactory

object ImageManager {
    fun getImageSize(uri: String): List<Int>{
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(uri, options)
        return listOf(options.outWidth, options.outHeight)
    }
}