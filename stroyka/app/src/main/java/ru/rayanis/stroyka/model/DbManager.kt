package ru.rayanis.stroyka.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    val db = Firebase.database.getReference("main")
    val auth = Firebase.auth

    fun publishObjectStroy(objectStroy: ObjectStroy, finishListener: FinishWorkListener) {
        if (auth.uid != null) db.child(objectStroy
            .key ?: "empty")
            .child(auth.uid!!)
            .child("object")
            .setValue(objectStroy)
            .addOnCompleteListener {
                finishListener.onFinish()
            }
    }

    fun getMyObjectStroy(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild(auth.uid + "/ad/uid")
            .equalTo(auth.uid)
        readDataFromDb(query, readDataCallback)
    }

    fun getAllObjectStroy(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild(auth.uid + "/ad/area")
        readDataFromDb(query, readDataCallback)
    }

    fun deleteObjectStroy(objectStroy: ObjectStroy, listener: FinishWorkListener) {
        if (objectStroy.key == null || objectStroy.uid == null) return
        db.child(objectStroy.key).child(objectStroy.uid).removeValue().addOnCompleteListener {
            if (it.isSuccessful) listener.onFinish()
        }
    }

    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?) {
        query.addListenerForSingleValueEvent(object: ValueEventListener{
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

    interface ReadDataCallback {
        fun readData(list: ArrayList<ObjectStroy>)
    }

    interface FinishWorkListener{
        fun onFinish()
    }
}