package ru.rayanis.tabladeanuncioskotlin.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.rayanis.tabladeanuncioskotlin.databinding.ActivityDescriptionBinding

class DescriptionActivity : AppCompatActivity() {
    lateinit var b: ActivityDescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(b.root)
    }
}