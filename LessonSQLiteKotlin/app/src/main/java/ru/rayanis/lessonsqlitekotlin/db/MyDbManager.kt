package ru.rayanis.lessonsqlitekotlin.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.icu.text.CaseMap
import android.provider.BaseColumns

class MyDbManager(context: Context) {
    private val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    //функция открытия базы данных
    fun openDb() {
        db = myDbHelper.writableDatabase
    }

    //функция добавления в базу данных
    fun insertToDb(title: String, content: String, uri: String) {
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
            put(MyDbNameClass.COLUMN_NAME_URI, uri)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }

    fun updateItem(title: String, content: String, uri: String, id: Int) {
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
            put(MyDbNameClass.COLUMN_NAME_URI, uri)
        }
        db?.update(MyDbNameClass.TABLE_NAME, values, selection, null)
    }

    //функция удаления из базы данных
    fun removeItemFromDb(id: String) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME, selection, null)
    }

    //функция чтения базы данных
    fun readDbData(searchText: String): ArrayList<ListItem> {
        val dataList = ArrayList<ListItem>()
        val selection = "${MyDbNameClass.COLUMN_NAME_TITLE} like ?"
        val cursor = db?.query(
            MyDbNameClass.TABLE_NAME, null, selection,
            arrayOf("%$searchText%"), null, null, null
        )

        while (cursor?.moveToNext()!!) {
            val dataTitle = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))
            val dataContent =
                cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_CONTENT))
            val dataUri = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_URI))
            val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val item = ListItem()
            item.title = dataTitle
            item.desc = dataContent
            item.uri = dataUri
            item.id = dataId
            dataList.add(item)
        }
        cursor.close()
        return dataList
    }

    fun closeDb() {
        myDbHelper.close()
    }

}