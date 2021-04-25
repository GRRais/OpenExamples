package ru.rayanis.lessonsqlitekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ru.rayanis.lessonsqlitekotlin.databinding.ActivityMainBinding
import ru.rayanis.lessonsqlitekotlin.db.MyAdapter
import ru.rayanis.lessonsqlitekotlin.db.MyDbManager

class MainActivity : AppCompatActivity() {

    private val myDbManager = MyDbManager(this)
    private val myAdapter = MyAdapter(ArrayList(), this)

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        init()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    fun onClickNew(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    private fun init() {
        b.rcView.layoutManager = LinearLayoutManager(this)
        b.rcView.adapter = myAdapter
    }

    private fun fillAdapter() {

        val list = myDbManager.readDbData()
        myAdapter.updateAdapter(list)

        if (list.size > 0) {
            b.tvNoElements.visibility = View.GONE
        } else {
            b.tvNoElements.visibility = View.VISIBLE
        }
    }
}