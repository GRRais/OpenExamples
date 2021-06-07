package ru.rayanis.lessonsqlitekotlin

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rayanis.lessonsqlitekotlin.databinding.EditActivityBinding
import ru.rayanis.lessonsqlitekotlin.db.MyDbManager
import ru.rayanis.lessonsqlitekotlin.db.MyIntentConstants
import java.util.*

class EditActivity : AppCompatActivity() {

    private lateinit var b: EditActivityBinding


    var id = 0
    var isEditState = false
    private val imageRequestCode = 10
    var tempImageUri = "empty"
    private val myDbManager = MyDbManager(this)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = EditActivityBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        getMyIntents()
        Log.d("MyLog", "Time: " + getCurrentTime())
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
            contentResolver.takePersistableUriPermission(
                data?.data!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    fun onClickAddImage(view: View) {
        b.mainImageLayout.visibility = View.VISIBLE
        b.fbAddImage.visibility = View.GONE
    }

    fun onClickDeleteImage(view: View) {
        b.mainImageLayout.visibility = View.GONE
        b.fbAddImage.visibility = View.VISIBLE
        tempImageUri = "empty"
    }

    fun onClickChooseImage(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestCode)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun onClickSave(view: View) {
        val myTitle = b.edTitle.text.toString()
        val myDesc = b.edDesc.text.toString()

        if (myTitle != "" && myDesc != "") {

            if (isEditState) {
                myDbManager.updateItem(myTitle, myDesc, tempImageUri, id, getCurrentTime())
            } else {
                myDbManager.insertToDb(myTitle, myDesc, tempImageUri, getCurrentTime())
            }
            finish()
//            CoroutineScope(Dispatchers.Main).launch {
//                if (isEditState) {
//                    myDbManager.updateItem(myTitle, myDesc, tempImageUri, id, getCurrentTime())
//                } else {
//                    myDbManager.insertToDb(myTitle, myDesc, tempImageUri, getCurrentTime())
//                }
//                finish()
//            }

        }

    }

    fun onEditEnable(view: View) {
        b.edTitle.isEnabled = true
        b.edDesc.isEnabled = true
        b.fbEdit.visibility = View.GONE
        b.fbAddImage.visibility = View.VISIBLE
        if (tempImageUri == "empty") return
        b.imButtonEditImage.visibility = View.VISIBLE
        b.imButtonDeleteImage.visibility = View.VISIBLE
    }

    private fun getMyIntents() {
        b.fbEdit.visibility = View.GONE
        val i = intent
        if (i != null) {
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {
                b.fbAddImage.visibility = View.GONE
                b.edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                isEditState = true
                b.edTitle.isEnabled = false
                b.edDesc.isEnabled = false
                b.fbEdit.visibility = View.VISIBLE
                b.edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
                if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty") {
                    b.mainImageLayout.visibility = View.VISIBLE
                    tempImageUri = i.getStringExtra(MyIntentConstants.I_URI_KEY)!!
                    b.imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
                    b.imButtonDeleteImage.visibility = View.GONE
                    b.imButtonEditImage.visibility = View.GONE
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }

}