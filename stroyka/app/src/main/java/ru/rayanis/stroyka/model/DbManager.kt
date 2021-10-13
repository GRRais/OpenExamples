package ru.rayanis.stroyka.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    val db = Firebase.database.getReference(MAIN_NODE)
    val auth = Firebase.auth

    //добавление объекта в БД
    fun publishObjectStroy(objectStroy: ObjectStroy, finishListener: FinishWorkListener) {
        if (auth.uid != null) db.child(objectStroy.key ?: "empty")
            .child(auth.uid!!)
            .child(OBJSTROY_NODE)
            .setValue(objectStroy)
            .addOnCompleteListener {
                finishListener.onFinish()
            }
    }

    //удаление объекта из БД
    fun deleteObjectStroy(objectStroy: ObjectStroy, listener: FinishWorkListener) {
        if (objectStroy.key == null || objectStroy.uid == null) return
        db.child(objectStroy.key).child(objectStroy.uid).removeValue().addOnCompleteListener {
            if (it.isSuccessful) listener.onFinish()
        }
    }

    fun onActiveClick(objectStroy: ObjectStroy, listener: FinishWorkListener) {
        if (objectStroy.isActive) {
            removeFromActive(objectStroy, listener)
        } else {
            addToActive(objectStroy, listener)
        }
    }

    //добавление объекта в активные
    private fun addToActive(objectStroy: ObjectStroy, listener: FinishWorkListener) {
        objectStroy.key?.let {
            db.child(it)
                .child(FAVS_NODE)
                .setValue(true)
                .addOnCompleteListener {
                    if (it.isSuccessful) listener.onFinish()
                }
        }
    }

    //удаление объекта из активных
    private fun removeFromActive(objectStroy: ObjectStroy, listener: FinishWorkListener) {
        objectStroy.key?.let {
            db.child(it)
                .child(FAVS_NODE)
                .child("active")
                .removeValue()
                .addOnCompleteListener {
                    if (it.isSuccessful) listener.onFinish()
                }
        }
    }

    //Чтение активных объявлений с БД
    fun getMyObjectStroy(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild(auth.uid + "/ad/uid")
            .equalTo(auth.uid)
        readDataFromDb(query, readDataCallback)
    }

    //чтение всех объявлений с БД
    fun getAllObjectStroy(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild(auth.uid + "/ad/area")
        readDataFromDb(query, readDataCallback)
    }

    //чтение данных с БД
    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?) {
        query.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val objectStroyArray = ArrayList<ObjectStroy>()
                for (item in snapshot.children) {
                    val objectStroy = item.children.iterator().next().child(OBJSTROY_NODE).getValue(ObjectStroy::class.java)
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

    companion object {
        const val OBJSTROY_NODE = "ad"
        const val FAVS_NODE = "active"
        const val MAIN_NODE = "main"
    }
}