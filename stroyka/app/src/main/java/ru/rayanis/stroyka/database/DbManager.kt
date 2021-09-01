package ru.rayanis.stroyka.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.rayanis.stroyka.data.ObjectStroy

class DbManager {
    val db = Firebase.database.getReference("main")
    private val auth = Firebase.auth

    fun publishObjectStroy(objectStroy: ObjectStroy) {
        if (auth.uid != null) db.child(objectStroy.key ?: "empty").child(auth.uid!!)
            .child("object").setValue(objectStroy)
    }
}