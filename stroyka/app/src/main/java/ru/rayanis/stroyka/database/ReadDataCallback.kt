package ru.rayanis.stroyka.database

import ru.rayanis.stroyka.data.ObjectStroy

interface ReadDataCallback {
    fun readData(list: List<ObjectStroy>)
}