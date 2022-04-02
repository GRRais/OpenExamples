package ru.rayanis.shoppinglist.activities

import android.app.Application
import ru.rayanis.shoppinglist.db.MainDataBase

class MainApp : Application() {
    val database by lazy { MainDataBase.getDataBase(this) }
}