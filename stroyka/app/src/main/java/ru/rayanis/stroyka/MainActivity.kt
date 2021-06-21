package ru.rayanis.stroyka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import ru.rayanis.stroyka.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        init()

    }

    private fun init() {
        var toggle = ActionBarDrawerToggle(this, b.drawerLayout, b.mainContent.toolbar, R.string.open, R.string.close)
        b.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }
}