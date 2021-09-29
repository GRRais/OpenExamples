package ru.rayanis.stroyka.act

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.fxn.utility.PermUtil
import ru.rayanis.stroyka.MainActivity
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.adapters.ImageAdapter
import ru.rayanis.stroyka.model.ObjectStroy
import ru.rayanis.stroyka.model.DbManager
import ru.rayanis.stroyka.databinding.ActivityEditObjectsBinding
import ru.rayanis.stroyka.dialogs.DialogSpinnerHelper
import ru.rayanis.stroyka.frag.FragmentCloseInterface
import ru.rayanis.stroyka.frag.ImageListFrag
import ru.rayanis.stroyka.utils.ImagePicker
import ru.rayanis.stroyka.utils.VillageHelper

class EditObjectStroyAct : AppCompatActivity(), FragmentCloseInterface {

    var chooseImageFrag: ImageListFrag? = null
    lateinit var b : ActivityEditObjectsBinding
    private val dialog = DialogSpinnerHelper()
    lateinit var imageAdapter: ImageAdapter
    private val dbManager = DbManager()
    var launcherMultiSelectImage: ActivityResultLauncher<Intent>? = null
    var launcherSingleSelectImage: ActivityResultLauncher<Intent>? = null
    var editImagePos = 0
    private var isEditState = false
    private var objectStroy: ObjectStroy? = null

    var isImagesPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEditObjectsBinding.inflate(layoutInflater)
        setContentView(b.root)
        init()
        checkEditState()
    }

    private fun checkEditState() {
        isEditState = isEditState()
        if (isEditState) {
            objectStroy = intent.getSerializableExtra(MainActivity.ADS_DATA) as ObjectStroy
            if (objectStroy != null) fillViews(objectStroy!!)
        }
    }

    private fun isEditState(): Boolean {
        return intent.getBooleanExtra(MainActivity.EDIT_STATE, false)
    }

    private fun fillViews(objectStroy: ObjectStroy) = with(b) {
        tvArea.text = objectStroy.area
        tvVillage.text = objectStroy.village
        tvOrganization.text = objectStroy.organization
        edDescription.setText(objectStroy.description)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //ImagePicker.getImages(this, 3, ImagePicker.REQUEST_CODE_GET_IMAGES)
                } else {
                    isImagesPermissionGranted = false
                    Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePicker",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun init(){
        imageAdapter = ImageAdapter()
        b.vpImages.adapter = imageAdapter
        launcherMultiSelectImage = ImagePicker.getLauncherForMultiSelectImages(this)
        launcherSingleSelectImage = ImagePicker.getLauncherForSingleImage(this)
    }

    //OnClicks
    fun onClickSelectArea(view: View) {
        val listAreas = VillageHelper.getAllAreas(this)
        dialog.showSpinnerDialog(this, listAreas, b.tvArea)
        if (b.tvVillage.text.toString() != getString(R.string.select_village)) {
            b.tvVillage.text = getString(R.string.select_village)
        }
    }

    fun onClickSelectVillage(view: View){
        val selectedArea = b.tvArea.text.toString()
        if (selectedArea != getString(R.string.select_area)) {
            val listVillage = VillageHelper.getAllVillages(selectedArea, this)
            dialog.showSpinnerDialog(this, listVillage, b.tvVillage)
        } else {
            Toast.makeText(this, "No area selected", Toast.LENGTH_LONG).show()
        }
    }

    fun onClickGetImages(view: View) {
        if (imageAdapter.mainArray.size == 0) {
            ImagePicker.launcher(this, launcherMultiSelectImage, 3)
        } else {
            openChooseImageFrag(null)
            chooseImageFrag?.updateAdapterFromEdit(imageAdapter.mainArray)
        }
    }

    fun onClickPublish(view: View) {
        val objectStroyTemp = fillObjectStroy()
        if (isEditState) {
            dbManager.publishObjectStroy(objectStroyTemp.copy(key = objectStroy?.key), onPublishFinish())
        } else {
            dbManager.publishObjectStroy(objectStroyTemp, onPublishFinish())
        }
    }

    private fun onPublishFinish(): DbManager.FinishWorkListener {
        return  object: DbManager.FinishWorkListener {
            override fun onFinish() {
                finish()
            }
        }
    }

    private fun fillObjectStroy(): ObjectStroy {
        val objectStroy: ObjectStroy
        b.apply {
            objectStroy = ObjectStroy(
                tvArea.text.toString(),
                tvVillage.text.toString(),
                tvOrganization.text.toString(),
                edDescription.text.toString(),
                dbManager.db.push().key,
                dbManager.auth.uid
            )
            return objectStroy
        }
    }

    override fun onFragClose(list: ArrayList<Bitmap>) {
        b.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFrag = null
    }

    fun openChooseImageFrag(newList: ArrayList<String>?) {
        chooseImageFrag = ImageListFrag(this, newList)
        b.scrollViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFrag!!)
        fm.commit()

    }
}