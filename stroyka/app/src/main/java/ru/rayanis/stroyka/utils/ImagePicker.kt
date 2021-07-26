package ru.rayanis.stroyka.utils

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity

object ImagePicker {
    const val REQUEST_CODE_GET_IMAGES = 999
    fun getImages(context: AppCompatActivity, imageCounter: Int) {
        val options = Options.init()
            .setRequestCode(REQUEST_CODE_GET_IMAGES)                                           //Request code for activity results
            .setCount(imageCounter)                                                   //Number of images to restict selection count
            .setFrontfacing(false)                                          //Span count for gallery min 1 & max 5
            .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
            .setPath("/pix/images")                                       //Custom Path For media Storage

        Pix.start(context, options)
    }
}