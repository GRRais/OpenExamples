package ru.rayanis.stroyka.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.rayanis.stroyka.model.DbManager
import ru.rayanis.stroyka.model.ObjectStroy

class FirebaseViewModel: ViewModel() {
    private val dbManager = DbManager()
    val liveObjStroyData = MutableLiveData<ArrayList<ObjectStroy>>()

    //загружаем все объекты строительства
    fun loadAllObjStroy() {
        dbManager.getAllObjectStroy(object: DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<ObjectStroy>) {
                liveObjStroyData.value = list
            }
        })
    }

    //загружаем активные объекты строительства
    fun loadActiveObjStroy() {
        dbManager.getActiveObjStroy(object: DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<ObjectStroy>) {
                liveObjStroyData.value = list
            }
        })
    }

    //обработка нажатия на Добавить в активные (сердечко)
    fun onActiveClick(objStroy: ObjectStroy) {
        dbManager.onActiveClick(objStroy, object: DbManager.FinishWorkListener {
            override fun onFinish() {
                val updatedList = liveObjStroyData.value
                val pos = updatedList?.indexOf(objStroy)
                if (pos != -1) {
                    pos?.let{
                        updatedList[pos] = updatedList[pos].copy(isActive = !objStroy.isActive)
                    }
                }
                liveObjStroyData.postValue(updatedList)
            }
        })
    }

    //удаление объекта из базы данных
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