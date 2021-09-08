package ru.rayanis.stroyka.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.rayanis.stroyka.data.ObjectStroy

class DbManager(val readDataCallback: ReadDataCallback?) {
    val db = Firebase.database.getReference("main")
    private val auth = Firebase.auth

    fun publishObjectStroy(objectStroy: ObjectStroy) {
        if (auth.uid != null) db.child(objectStroy.key ?: "empty").child(auth.uid!!)
            .child("object").setValue(objectStroy)
    }

    fun readDataFromDb() {
        db.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val objectStroyArray = ArrayList<ObjectStroy>()
                for (item in snapshot.children) {
                    val objectStroy = item.children.iterator().next().child("object").getValue(ObjectStroy::class.java)
                    if (objectStroy != null) objectStroyArray.add(objectStroy)
                }
                readDataCallback?.readData(objectStroyArray)
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
}