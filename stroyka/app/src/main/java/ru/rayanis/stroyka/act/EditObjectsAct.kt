package ru.rayanis.stroyka.act

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.rayanis.stroyka.MainActivity
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.adapters.ImageAdapter
import ru.rayanis.stroyka.databinding.ActivityEditObjectsBinding
import ru.rayanis.stroyka.dialogs.DialogSpinnerHelper
import ru.rayanis.stroyka.frag.FragmentCloseInterface
import ru.rayanis.stroyka.frag.ImageListFrag
import ru.rayanis.stroyka.model.DbManager
import ru.rayanis.stroyka.model.ObjectStroy
import ru.rayanis.stroyka.utils.ImagePicker
import ru.rayanis.stroyka.utils.VillageHelper

class EditObjectsAct: AppCompatActivity(), FragmentCloseInterface {

    var chooseImageFrag: ImageListFrag? = null
    lateinit var b: ActivityEditObjectsBinding
    private val dialog = DialogSpinnerHelper()
    lateinit var imageAdapter: ImageAdapter
    private val dbManager = DbManager()
    var editImagePos = 0
    private var isEditState = false
    private var objStroy: ObjectStroy? = null

    var isImagesPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEditObjectsBinding.inflate(layoutInflater)
        setContentView(b.root)
        init()
        checkEditState()
    }

    //проверяем, если editState
    private fun checkEditState() {
        isEditState = isEditState()
        if (isEditState) {
            objStroy = intent.getSerializableExtra(MainActivity.OBJSTROY_DATA) as ObjectStroy
            if (objStroy != null) fillViews(objStroy!!)
        }
    }

    //проверяем, зашли для создания нового объявления или редактирования
    private fun isEditState(): Boolean {
        return intent.getBooleanExtra(MainActivity.EDIT_STATE, false)
    }

    //заполняем вьюшки данными объекта класса ObjectStroy
    private fun fillViews(objStroy: ObjectStroy) = with(b) {
        tvArea.text = objStroy.area
        tvVillage.text = objStroy.village
        tvOrganization.text = objStroy.organization
        edDescription.setText(objStroy.description)
        bCreateEditObject.text = getString(R.string.change)
    }

    private fun init(){
        imageAdapter = ImageAdapter()
        b.vpImages.adapter = imageAdapter
    }

    //обработка нажатия кнопки Выбрать район
    fun onClickSelectArea(view: View) {
        val listAreas = VillageHelper.getAllAreas(this)
        dialog.showSpinnerDialog(this, listAreas, b.tvArea)
        if (b.tvVillage.text.toString() != getString(R.string.select_village)) {
            b.tvVillage.text = getString(R.string.select_village)
        }
    }

    //обработка нажатия кнопки Выбрать деревню
    fun onClickSelectVillage(view: View){
        val selectedArea = b.tvArea.text.toString()
        if (selectedArea != getString(R.string.select_area)) {
            val listVillage = VillageHelper.getAllVillages(selectedArea, this)
            dialog.showSpinnerDialog(this, listVillage, b.tvVillage)
        } else {
            Toast.makeText(this, "No area selected", Toast.LENGTH_LONG).show()
        }
    }

    //обработка нажатия кнопки Добавить/изменить изображения
    fun onClickGetImages(view: View) {
        if (imageAdapter.mainArray.size == 0) {
            ImagePicker.getMultiImages(this,3)
        } else {
            openChooseImageFrag(null)
            chooseImageFrag?.updateAdapterFromEdit(imageAdapter.mainArray)
        }
    }

    // обработка нажатия кнопки Опубликовать,
    // выполняем проверку isEditState, если true -> редактируем объект,
    // иначе публикуем новый
    fun onClickPublish(view: View) {
        val objStroyTemp = fillObjectStroy()
        if (isEditState) {
            Log.d("MyLog", "isEditState=${isEditState}")
            objStroyTemp.copy(key = objStroy?.key).let { dbManager.publishObjectStroy(it, onPublishFinish()) }
        } else {
            dbManager.publishObjectStroy(objStroyTemp, onPublishFinish())
        }
    }


    private fun onPublishFinish(): DbManager.FinishWorkListener {
        return  object: DbManager.FinishWorkListener {
            override fun onFinish() {
                finish()
            }
        }
    }

    //заполнение экземпляра объекта класса ObjectStroy
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

    fun openChooseImageFrag(newList: ArrayList<Uri>?) {
        chooseImageFrag = ImageListFrag(this)
        if (newList != null) chooseImageFrag?.resizeSelectedImages(newList, true, this)
        b.scrollViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFrag!!)
        fm.commit()

    }
}