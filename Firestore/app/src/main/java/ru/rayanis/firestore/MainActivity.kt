package ru.rayanis.firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.rayanis.firestore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val personCollectionRef = Firebase.firestore.collection("persons")
    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        b.btnUploadData.setOnClickListener {
            val firstName = b.etFirstName.text.toString()
            val lastName = b.etLastName.text.toString()
            val age = b.etAge.text.toString()
            val person = Person(firstName, lastName, age)
            savePerson(person)
        }
    }

    private fun savePerson(person: Person)  = CoroutineScope(Dispatchers.IO).launch {
        try {
            personCollectionRef.add(person).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Successfully saved data.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}