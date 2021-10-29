package ru.rayanis.stroyka.act

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import ru.rayanis.stroyka.MainActivity
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.adapters.ImageAdapter
import ru.rayanis.stroyka.databinding.ActivityEditObjectsBinding
import ru.rayanis.stroyka.dialogs.DialogSpinnerHelper
import ru.rayanis.stroyka.frag.FragmentCloseInterface
import ru.rayanis.stroyka.frag.ImageListFrag
import ru.rayanis.stroyka.model.DbManager
import ru.rayanis.stroyka.model.ObjectStroy
import ru.rayanis.stroyka.utils.ImageManager
import ru.rayanis.stroyka.utils.ImagePicker
import ru.rayanis.stroyka.utils.VillageHelper
import java.io.ByteArrayOutputStream

class EditObjectsAct: AppCompatActivity(), FragmentCloseInterface {

    var chooseImageFrag: ImageListFrag? = null
    lateinit var b: ActivityEditObjectsBinding
    private val dialog = DialogSpinnerHelper()
    lateinit var imageAdapter: ImageAdapter
    private val dbManager = DbManager()
    var editImagePos = 0
    private var imageIndex = 0
    private var isEditState = false
    private var objStroy: ObjectStroy? = null

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
        ImageManager.fillImageArray(objStroy, imageAdapter)
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
        objStroy = fillObjectStroy()
        if (isEditState) {
            objStroy?.copy(key = objStroy?.key)?.let { dbManager.publishObjectStroy(it, onPublishFinish()) }
        } else {
            uploadImages()
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
        val objStroy: ObjectStroy
        b.apply {
            objStroy = ObjectStroy(
                tvArea.text.toString(),
                tvVillage.text.toString(),
                tvOrganization.text.toString(),
                edDescription.text.toString(),
                "empty",
                "empty",
                "empty",
                dbManager.db.push().key,
                dbManager.auth.uid,
                System.currentTimeMillis().toString())
        }
        return objStroy
    }

    override fun onFragClose(list: ArrayList<Bitmap>) {
        b.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFrag = null
    }

    //открываем фрагмент с выбранными картинками
    fun openChooseImageFrag(newList: ArrayList<Uri>?) {
        chooseImageFrag = ImageListFrag(this)
        if (newList != null) chooseImageFrag?.resizeSelectedImages(newList, true, this)
        b.scrollViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFrag!!)
        fm.commit()
    }

    //загружаем картинки, если они есть в EditObjStroy
    private fun uploadImages() {
        if (imageAdapter.mainArray.size == imageIndex) {
            dbManager.publishObjectStroy(objStroy!!, onPublishFinish())
            return
        }
        //загружаем картинку в storage
        val byteArray = prepareImageByteArray(imageAdapter.mainArray[imageIndex])
        uploadImage(byteArray) {
            nextImage(it.result.toString())
        }
    }

    private fun nextImage(uri: String) {
        setImageUriToObjStroy(uri)
        imageIndex++
        uploadImages()
    }

    //записываем ссылки на картинки в objStroy
    private fun setImageUriToObjStroy(uri: String) {
        when(imageIndex) {
            0 -> objStroy = objStroy?.copy(mainImage = uri)
            1 -> objStroy = objStroy?.copy(image2 = uri)
            2 -> objStroy = objStroy?.copy(image3 = uri)
        }
    }

    //подготавливаем изображение изменяя размер и превращая в ByteArray
    private fun prepareImageByteArray(bitmap: Bitmap): ByteArray {
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream)
        return outStream.toByteArray()
    }

    //загружаем изображение и выдает ссылку, получив ссылку запускает интерфейс который передаем
    private fun uploadImage(byteArray: ByteArray, listener: OnCompleteListener<Uri>) {
        val imStorageRef = dbManager.dbStorage
            .child(dbManager.auth.uid!!)
            .child("image_${System.currentTimeMillis()}")
        val upTask = imStorageRef.putBytes(byteArray)
        upTask.continueWithTask {
            task -> imStorageRef.downloadUrl
        }.addOnCompleteListener(listener)
    }
}