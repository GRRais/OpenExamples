package ru.rayanis.lessonsqlitekotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.rayanis.lessonsqlitekotlin.databinding.EditActivityBinding
import ru.rayanis.lessonsqlitekotlin.db.MyDbManager

class EditActivity : AppCompatActivity() {

    private lateinit var b: EditActivityBinding
    val myDbManager = MyDbManager(this)


    val imageRequestCode = 10
    var tempImageUri = "empty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = EditActivityBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode){
            b.imMainImage.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
        }
    }

    fun onClickAddImage(view: View) {
        b.mainImageLayout.visibility = View.VISIBLE
        b.fbAddImage.visibility = View.GONE
    }

    fun onClickDeleteImage(view: View) {
        b.mainImageLayout.visibility = View.GONE
        b.fbAddImage.visibility = View.VISIBLE
    }

    fun onClickChooseImage(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestCode)
    }

    fun onClickSave(view: View) {
        val myTitle = b.edTitle.text.toString()
        val myDesc = b.edDesc.text.toString()

        if (myTitle != "" && myDesc != "") {
            myDbManager.insertToDb(myTitle, myDesc, tempImageUri)

        }
    }

}