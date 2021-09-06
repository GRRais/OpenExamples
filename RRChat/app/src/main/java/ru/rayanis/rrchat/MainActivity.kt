package ru.rayanis.rrchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Firebase.database("https://rrchat-334c4-default-rtdb.europe-west1.firebasedatabase.app")
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
    }
}