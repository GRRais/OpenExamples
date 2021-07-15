package ru.rayanis.stroyka.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.databinding.ActivityEditObjectsBinding
import ru.rayanis.stroyka.utils.VillageHelper

class EditObjectsAct : AppCompatActivity() {
    private lateinit var b : ActivityEditObjectsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_objects)
        b = ActivityEditObjectsBinding.inflate(layoutInflater)
        setContentView(b.root)
        val listAreas = VillageHelper.getAllAreas(this)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, VillageHelper.getAllAreas(this))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        b.spArea.adapter = adapter
    }
}