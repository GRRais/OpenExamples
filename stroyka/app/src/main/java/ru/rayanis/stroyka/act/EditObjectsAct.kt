package ru.rayanis.stroyka.act

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.databinding.ActivityEditObjectsBinding
import ru.rayanis.stroyka.dialogs.DialogSpinnerHelper
import ru.rayanis.stroyka.utils.VillageHelper

class EditObjectsAct : AppCompatActivity() {

    private lateinit var b : ActivityEditObjectsBinding
    private var dialog = DialogSpinnerHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_objects)
        b = ActivityEditObjectsBinding.inflate(layoutInflater)
        setContentView(b.root)
        init()
    }
    private fun init(){
    }
    //OnClicks
    fun onClickSelectArea(view: View){
        val listAreas = VillageHelper.getAllAreas(this)
        dialog.showSpinnerDialog(this, listAreas)
    }
}