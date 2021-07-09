package ru.rayanis.stroyka.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.databinding.ActivityEditObjectsBinding

class EditObjectsAct : AppCompatActivity() {
    private lateinit var b : ActivityEditObjectsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_objects)
        b = ActivityEditObjectsBinding.inflate(layoutInflater)
        setContentView(b.root)
    }
}