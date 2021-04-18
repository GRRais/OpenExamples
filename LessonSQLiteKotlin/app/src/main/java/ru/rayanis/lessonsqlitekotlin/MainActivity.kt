package ru.rayanis.lessonsqlitekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.rayanis.lessonsqlitekotlin.databinding.ActivityMainBinding
import ru.rayanis.lessonsqlitekotlin.db.MyDbManager

class MainActivity : AppCompatActivity() {

    val myDbManager = MyDbManager(this)

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        val dataList = myDbManager.readDbData()
        for (item in dataList) {
            binding.tvTest.append(item)
            binding.tvTest.append("\n")
        }
    }

    fun onClickSave(view: View) {
        binding.tvTest.text = ""
        myDbManager.openDb()
        myDbManager.insertToDb(binding.edTitle.text.toString(), binding.edContext.toString())
        val dataList = myDbManager.readDbData()
        for (item in dataList) {
            binding.tvTest.append(item)
            binding.tvTest.append("\n")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }
}