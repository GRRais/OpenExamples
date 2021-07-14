package ru.rayanis.stroyka.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.rayanis.stroyka.R
import ru.rayanis.stroyka.databinding.ActivityCreateEditMaterialBinding

class CreateEditMaterialAct : AppCompatActivity() {
    private lateinit var b: ActivityCreateEditMaterialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCreateEditMaterialBinding.inflate(layoutInflater)
        setContentView(b.root)
    }
}