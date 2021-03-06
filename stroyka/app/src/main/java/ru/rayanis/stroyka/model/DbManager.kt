package ru.rayanis.stroyka.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DbManager {
    val db = Firebase.database.getReference(MAIN_NODE)
    val dbStorage = Firebase.storage.getReference(MAIN_NODE)
    val auth = Firebase.auth

    //добавление объекта в БД
    fun publishObjectStroy(objStroy: ObjectStroy, finishListener: FinishWorkListener) {
        if (auth.uid != null) db.child(objStroy.key ?: "empty")
            .child(auth.uid!!)
            .child(OBJSTROY_NODE)
            .setValue(objStroy)
            .addOnCompleteListener {
                val objStroyFilter = ObjectStroyFilter(objStroy.time,
                    "${objStroy.organization}_${objStroy.time}"
                )
                db.child(objStroy.key ?: "empty")
                    .child(FILTER_NODE)
                    .setValue(objStroyFilter)
                    .addOnCompleteListener {
                        finishListener.onFinish()
                    }
            }
    }

    //удаление объекта из БД
    fun deleteObjStroy(objectStroy: ObjectStroy, listener: FinishWorkListener) {
        if (objectStroy.key == null || objectStroy.uid == null) return
        db.child(objectStroy.key).removeValue().addOnCompleteListener {
            if (it.isSuccessful) listener.onFinish()
        }
    }

    //обрабатываем нажатие на сердечко
    fun onActiveClick(objStroy: ObjectStroy, listener: FinishWorkListener) {
        if (objStroy.isActive) {
            removeFromActive(objStroy, listener)
        } else {
            addToActive(objStroy, listener)
        }
    }

    //добавление объекта в активные
    private fun addToActive(objStroy: ObjectStroy, listener: FinishWorkListener) {
        objStroy.key?.let {
            db.child(it)
                .child(ISACTIVE_NODE)
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
                .child(ISACTIVE_NODE)
                .removeValue()
                .addOnCompleteListener {
                    if (it.isSuccessful) listener.onFinish()
                }
        }
    }

    //получить активные объекты с БД
    fun getActiveObjStroy(readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild("/isactive").equalTo(true)
        readDataFromDb(query, readDataCallback)
    }

    //чтение всех объявлений с БД
    fun getAllObjStroy(lastTime: String, readDataCallback: ReadDataCallback?) {
        val query = db.orderByChild( "/objStroyFilter/time")
            .startAfter(lastTime).limitToFirst(OBJSTROY_LIMIT)
        readDataFromDb(query, readDataCallback)
    }

//    //чтение всех объявлений с БД
//    fun getAllObjStroyFromOrg(lastOrgTime: String, readDataCallback: ReadDataCallback?) {
//        val query = db.orderByChild( "/objStroyFilter/orgTime")
//            .startAfter(lastOrgTime).limitToFirst(OBJSTROY_LIMIT)
//        readDataFromDb(query, readDataCallback)
//    }

    //чтение объектов с БД
    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?) {
        query.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val objStroyArray = ArrayList<ObjectStroy>()
                for (item in snapshot.children) {
                    var objStroy:ObjectStroy? = null
                    item.children.forEach {
                        if (objStroy == null) objStroy = it.child(OBJSTROY_NODE)
                            .getValue(ObjectStroy::class.java)
                    }
                    val isActive = item.child(ISACTIVE_NODE).getValue(Boolean::class.java)
                    objStroy?.isActive = isActive != null

                    if(objStroy != null) objStroyArray.add(objStroy!!)
                }
                readDataCallback?.readData(objStroyArray)
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
        const val OBJSTROY_NODE = "objStroy"
        const val FILTER_NODE = "objStroyFilter"
        const val ISACTIVE_NODE = "isActive"
        const val MAIN_NODE = "main"
        const val OBJSTROY_LIMIT = 2
    }
}