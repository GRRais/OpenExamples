package ru.rayanis.stroyka.act

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.rayanis.stroyka.adapters.ImageAdapter
import ru.rayanis.stroyka.databinding.ActivityDescriptionBinding
import ru.rayanis.stroyka.model.ObjectStroy
import ru.rayanis.stroyka.utils.ImageManager

class DescriptionActivity : AppCompatActivity() {
    lateinit var b: ActivityDescriptionBinding
    lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(b.root)
        init()
    }

    private fun init() {
        adapter= ImageAdapter()
        b.apply {
            viewPager2.adapter = adapter
        }
        getIntentFromMainAct()
        imageChangeCounter()
    }

    //получение из интента objStroy
    private fun getIntentFromMainAct() {
        val objStroy = intent.getSerializableExtra("OBJSTROY") as ObjectStroy
        updateUI(objStroy)
    }

    private fun updateUI(objStroy: ObjectStroy) {
        ImageManager.fillImageArray(objStroy, adapter)
        fillTextViews(objStroy)
    }

    private fun fillTextViews(objStroy: ObjectStroy) = with(b) {
        tvDescription.text = objStroy.description
        tvArea.text = objStroy.area
        tvVillage.text = objStroy.village
        tvOrganization.text = objStroy.organization
    }

    private fun imageChangeCounter() {
        b.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val imageCounter = "${position+1}/${b.viewPager2.adapter?.itemCount}"
                b.tvImageCounter.text = imageCounter
            }
        })
    }
}