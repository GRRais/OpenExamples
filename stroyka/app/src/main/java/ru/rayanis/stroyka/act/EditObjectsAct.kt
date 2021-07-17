package ru.rayanis.stroyka.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.databinding.ActivityEditObjectsBinding
import ru.rayanis.stroyka.dialogs.DialogSpinnerHelper
import ru.rayanis.stroyka.utils.VillageHelper

class EditObjectsAct : AppCompatActivity() {
    private lateinit var b : ActivityEditObjectsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_objects)
        b = ActivityEditObjectsBinding.inflate(layoutInflater)
        setContentView(b.root)
        val listAreas = VillageHelper.getAllAreas(this)
        val dialog = DialogSpinnerHelper()
        dialog.showSpinnerDialog(this, listAreas)

    }
}