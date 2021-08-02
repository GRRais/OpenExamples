package ru.rayanis.stroyka.act

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.adapters.ImageAdapter
import ru.rayanis.stroyka.databinding.ActivityEditObjectsBinding
import ru.rayanis.stroyka.dialogs.DialogSpinnerHelper
import ru.rayanis.stroyka.frag.FragmentCloseInterface
import ru.rayanis.stroyka.frag.ImageListFrag
import ru.rayanis.stroyka.frag.SelectImageItem
import ru.rayanis.stroyka.utils.ImagePicker
import ru.rayanis.stroyka.utils.VillageHelper

class EditObjectsAct : AppCompatActivity(), FragmentCloseInterface {

    private var chooseImageFrag: ImageListFrag? = null
    lateinit var b : ActivityEditObjectsBinding
    private var dialog = DialogSpinnerHelper()
    private lateinit var imageAdapter: ImageAdapter
    var isImagesPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_objects)
        b = ActivityEditObjectsBinding.inflate(layoutInflater)
        setContentView(b.root)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_IMAGES) {
            if (data != null) {
                val returnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                if (returnValues?.size!! > 1 && chooseImageFrag == null) {
                    chooseImageFrag = ImageListFrag(this, returnValues)
                    b.scrollViewMain.visibility = View.GONE
                    val fm = supportFragmentManager.beginTransaction()
                    fm.replace(R.id.place_holder, chooseImageFrag!!)
                    fm.commit()
                } else if (chooseImageFrag != null) {
                    chooseImageFrag?.updateAdapter(returnValues)
                }
            }
        }
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
                    ImagePicker.getImages(this, 3)
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
        ImagePicker.getImages(this, 3)
    }

    override fun onFragClose(list: ArrayList<SelectImageItem>) {
        b.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFrag = null
    }
}