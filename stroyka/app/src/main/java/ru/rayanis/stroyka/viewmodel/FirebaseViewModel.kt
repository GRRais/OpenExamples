package ru.rayanis.stroyka.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.rayanis.stroyka.model.DbManager
import ru.rayanis.stroyka.model.ObjectStroy

class FirebaseViewModel: ViewModel() {
    private val dbManager = DbManager()
    val liveObjStroyData = MutableLiveData<ArrayList<ObjectStroy>>()
    fun loadAllObjectStroy() {
        dbManager.getAllObjectStroy(object: DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<ObjectStroy>) {
                liveObjStroyData.value = list
            }
        })
    }

    fun onActiveClick(objectStroy: ObjectStroy) {
        dbManager.onActiveClick(objectStroy, object: DbManager.FinishWorkListener {
            override fun onFinish() {
                val updatedList = liveObjStroyData.value
                val pos = updatedList?.indexOf(objectStroy)
                if (pos != -1) {
                    pos?.let{
                        updatedList[pos] = updatedList[pos].copy(isActive = !objectStroy.isActive)
                    }
                }
                updatedList?.remove(objectStroy)
                liveObjStroyData.postValue(updatedList)
            }
        })
    }

    fun loadMyObjectStroy() {
        dbManager.getActiveObjStroy(object: DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<ObjectStroy>) {
                liveObjStroyData.value = list
            }
        })
    }

    fun deleteItem(objStroy: ObjectStroy) {
        dbManager.deleteObjStroy(objStroy, object: DbManager.FinishWorkListener {
            override fun onFinish() {
                val updatedList = liveObjStroyData.value
                updatedList?.remove(objStroy)
                liveObjStroyData.postValue(updatedList)
            }
        })
    }
}