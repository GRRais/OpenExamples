package ru.rayanis.stroyka.utils

import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rayanis.stroyka.act.EditObjectsAct

object ImagePicker {
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
    const val MAX_IMAGE_COUNT = 3
    private fun getOptions(imageCounter: Int): Options {
        val options = Options.init()
            .setCount(imageCounter)                                                   //Number of images to restict selection count
            .setFrontfacing(false)                                          //Span count for gallery min 1 & max 5
            .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
            .setPath("/pix/images")
        return options
    }

    fun launcher(edAct: EditObjectsAct, launcher: ActivityResultLauncher<Intent>?, imageCounter: Int) {
        PermUtil.checkForCamaraWritePermissions(edAct) {
            val intent = Intent(edAct, Pix::class.java).apply {
                putExtra("options", getOptions(imageCounter))
            }
            launcher?.launch(intent)
        }
    }

    fun getLauncherForMultiSelectImages(edAct: EditObjectsAct): ActivityResultLauncher<Intent> {
        return edAct.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {
                    val returnValues = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    if (returnValues?.size!! > 1 && edAct.chooseImageFrag == null) {
                        edAct.openChooseImageFrag(returnValues)
                    } else if (edAct.chooseImageFrag != null) {
                        edAct.chooseImageFrag?.updateAdapter(returnValues)
                    } else if (returnValues.size == 1 && edAct.chooseImageFrag == null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            edAct.b.pBarLoad.visibility = View.VISIBLE
                            val bitMapArray = ImageManager.imageResize(returnValues) as ArrayList<Bitmap>
                            edAct.b.pBarLoad.visibility = View.GONE
                            edAct.imageAdapter.update(bitMapArray)
                        }
                    }
                }
            }
        }
    }

    fun getLauncherForSingleImage(edAct: EditObjectsAct): ActivityResultLauncher<Intent> {
        return edAct.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {
                    val uris = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    edAct.chooseImageFrag?.setSingleImage(uris?.get(0)!!, edAct.editImagePos)
                }
            }
        }
    }
}