package ru.rayanis.tabladeanuncioskotlin.act

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.rayanis.tabladeanuncioskotlin.adapters.ImageAdapter
import ru.rayanis.tabladeanuncioskotlin.databinding.ActivityDescriptionBinding
import ru.rayanis.tabladeanuncioskotlin.model.Ad
import ru.rayanis.tabladeanuncioskotlin.utils.ImageManager

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
        adapter = ImageAdapter()
        b.apply {
            viewPager.adapter = adapter
        }
        getIntentFromMainAct()
    }

    private fun getIntentFromMainAct() {
        val ad = intent.getSerializableExtra("AD") as Ad
        fillImageArray(ad)
    }

    private fun fillImageArray(ad: Ad) {
        val listUris = listOf(ad.mainImage, ad.image2, ad.image3)
        CoroutineScope(Dispatchers.Main).launch {
            val bitmapList = ImageManager.getBitmapFromUris(listUris)
            adapter.update(bitmapList as ArrayList<Bitmap>)
        }
    }
}