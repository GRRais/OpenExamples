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

    fun loadMyObjectStroy() {
        dbManager.getMyObjectStroy(object: DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<ObjectStroy>) {
                liveObjectStroyData.value = list
            }
        })
    }
}