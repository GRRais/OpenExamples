package ru.rayanis.stroyka.utils

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.act.EditObjectsAct

object ImagePicker {
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
    const val MAX_IMAGE_COUNT = 3
    private fun getOptions(imageCounter: Int): Options {
        val options = Options().apply() {
            count = imageCounter
            isFrontFacing = false
            mode = Mode.Picture
            path = "/pix/images"
        }
        return options
    }

    //функция выбора более одной картинки
    fun getMultiImages(edAct: EditObjectsAct, imageCounter: Int) {
        edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    getMultiSelectImages(edAct, result.data)
                }
            }
        }
    }

    fun addImages(edAct: EditObjectsAct, imageCounter: Int) {
        val f = edAct.chooseImageFrag
        edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    edAct.chooseImageFrag = f
                    openChooseImageFrag(edAct, f!!)
                    edAct.chooseImageFrag?.updateAdapter(result.data as ArrayList<Uri>, edAct)

                }
            }
        }
    }

    //функция выбора одной картинки
    fun getSingleImage(edAct: EditObjectsAct) {
        val f = edAct.chooseImageFrag
        edAct.addPixToActivity(R.id.place_holder, getOptions(1)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    edAct.chooseImageFrag = f
                    openChooseImageFrag(edAct, f!!)
                    singleImage(edAct, result.data[0])
                }
            }
        }
    }

    private fun openChooseImageFrag(edAct: EditObjectsAct, f: Fragment) {
        edAct.supportFragmentManager.beginTransaction().replace(R.id.place_holder, f).commit()
    }

    //закрываем фрагмент с выбором картинок
    private fun closePixFragment(edAct: EditObjectsAct) {
        val fList = edAct.supportFragmentManager.fragments
        fList.forEach {
            if (it.isVisible) {
                edAct.supportFragmentManager.beginTransaction().remove(it).commit()
            }
        }
    }

    //обрабатывает выбранные картинки
    private fun getMultiSelectImages(edAct: EditObjectsAct, uris: List<Uri>) {
        if (uris.size > 1 && edAct.chooseImageFrag == null) {
            edAct.openChooseImageFrag(uris as ArrayList<Uri>)
        } else if (uris.size == 1 && edAct.chooseImageFrag == null) {
            CoroutineScope(Dispatchers.Main).launch {
                edAct.b.pBarLoad.visibility = View.VISIBLE
                val bitMapArray = ImageManager.imageResize(uris as ArrayList<Uri>, edAct) as ArrayList<Bitmap>
                edAct.b.pBarLoad.visibility = View.GONE
                edAct.imageAdapter.update(bitMapArray)
                closePixFragment(edAct)
            }
        }
    }

    //обрабатываем выбор одной картинки
    private fun singleImage(edAct: EditObjectsAct, uri: Uri) {
        edAct.chooseImageFrag?.setSingleImage(uri, edAct.editImagePos)
    }
}