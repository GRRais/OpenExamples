package ru.rayanis.lessonsqlitekotlin.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.icu.text.CaseMap
import android.provider.BaseColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyDbManager(context: Context) {
    private val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    //функция открытия базы данных
    fun openDb() {
        db = myDbHelper.writableDatabase
    }

    //функция добавления в базу данных
    suspend fun insertToDb(title: String, content: String, uri: String, time: String)= withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
            put(MyDbNameClass.COLUMN_NAME_URI, uri)
            put(MyDbNameClass.COLUMN_NAME_TIME, time)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }

    suspend fun updateItem(title: String, content: String, uri: String, id: Int, time: String) = withContext(Dispatchers.IO) {
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
            put(MyDbNameClass.COLUMN_NAME_URI, uri)
            put(MyDbNameClass.COLUMN_NAME_TIME, time)
        }
        db?.update(MyDbNameClass.TABLE_NAME, values, selection, null)
    }

    //функция удаления из базы данных
    fun removeItemFromDb(id: String) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME, selection, null)
    }

    //функция чтения базы данных
    suspend fun readDbData(searchText: String): ArrayList<ListItem> = withContext(Dispatchers.IO){
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
            val time = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TIME))
            val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val item = ListItem()
            item.title = dataTitle
            item.desc = dataContent
            item.uri = dataUri
            item.time = time
            item.id = dataId
            dataList.add(item)
        }
        cursor.close()
        return@withContext dataList
    }

    fun closeDb() {
        myDbHelper.close()
    }

}