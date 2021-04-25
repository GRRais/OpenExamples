package ru.rayanis.lessonsqlitekotlin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.rayanis.lessonsqlitekotlin.databinding.EditActivityBinding
import ru.rayanis.lessonsqlitekotlin.db.MyDbManager
import ru.rayanis.lessonsqlitekotlin.db.MyIntentConstants

class EditActivity : AppCompatActivity() {

    private lateinit var b: EditActivityBinding
    private val myDbManager = MyDbManager(this)


    val imageRequestCode = 10
    var tempImageUri = "empty"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = EditActivityBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        getMyIntents()
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
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {
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
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, imageRequestCode)
    }

    fun onClickSave(view: View) {
        val myTitle = b.edTitle.text.toString()
        val myDesc = b.edDesc.text.toString()

        if (myTitle != "" && myDesc != "") {
            myDbManager.insertToDb(myTitle, myDesc, tempImageUri)
            finish()
        }

    }

    private fun getMyIntents() {
        val i = intent
        if (i != null) {
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {
                b.fbAddImage.visibility = View.GONE
                b.edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                b.edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty") {
                    b.mainImageLayout.visibility = View.VISIBLE
                    b.imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
                    b.imButtonDeleteImage.visibility = View.GONE
                    b.imButtonEditImage.visibility = View.GONE
                }
            }
        }
    }

}