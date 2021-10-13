package ru.rayanis.stroyka.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.rayanis.stroyka.model.DbManager
import ru.rayanis.stroyka.model.ObjectStroy

class FirebaseViewModel: ViewModel() {
    private val dbManager = DbManager()
    val liveObjectStroyData = MutableLiveData<ArrayList<ObjectStroy>>()
    fun loadAllObjectStroy() {
        dbManager.getAllObjectStroy(object: DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<ObjectStroy>) {
                liveObjectStroyData.value = list
            }
        })
    }

    fun onActiveClick(objectStroy: ObjectStroy) {
        dbManager.onActiveClick(objectStroy, object: DbManager.FinishWorkListener {
            override fun onFinish() {
                TODO("Not yet implemented")
            }
        })
    }

    fun loadMyObjectStroy() {
        dbManager.getMyObjectStroy(object: DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<ObjectStroy>) {
                liveObjectStroyData.value = list
            }
        })
    }

    fun deleteItem(objectStroy: ObjectStroy) {
        dbManager.deleteObjectStroy(objectStroy, object: DbManager.FinishWorkListener {
            override fun onFinish() {
                val updatedList = liveObjectStroyData.value
                updatedList?.remove(objectStroy)
                liveObjectStroyData.postValue(updatedList)
            }
        })
    }
}