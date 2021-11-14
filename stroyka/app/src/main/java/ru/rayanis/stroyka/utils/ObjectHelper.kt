package ru.rayanis.stroyka.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException

object ObjectHelper {

    //считываем с файла, районы
    fun getAllAreas(context: Context): ArrayList<String> {
        val tempArray = ArrayList<String>()
        try {
            val inputStream = context.assets.open("areasToVillages.json")
            val size: Int = inputStream.available()
            val bytesArray = ByteArray(size)
            inputStream.read(bytesArray)
            val jsonFile = String(bytesArray)
            val jsonObject = JSONObject(jsonFile)
            val areasNames = jsonObject.names()
            if (areasNames != null) {
                for (n in 0 until areasNames.length()) {
                    tempArray.add(areasNames.getString(n))
                }
            }
        } catch (e: IOException) { }
        return tempArray
    }

    //считываем с файла, деревни
    fun getAllVillages(area: String, context: Context): ArrayList<String> {
        val tempArray = ArrayList<String>()
        try {
            val inputStream = context.assets.open("areasToVillages.json")
            val size: Int = inputStream.available()
            val bytesArray = ByteArray(size)
            inputStream.read(bytesArray)
            val jsonFile = String(bytesArray)
            val jsonObject = JSONObject(jsonFile)
            val villageNames = jsonObject.getJSONArray(area)

                for (n in 0 until villageNames.length()) {
                    tempArray.add(villageNames.getString(n))
                }
        } catch (e: IOException) { }
        return tempArray
    }

    //считываем с файла, организации
    fun getAllOrganizations(context: Context): ArrayList<String> {
        val tempArray = ArrayList<String>()
        try {
            val inputStream = context.assets.open("organizations.json")
            val size: Int = inputStream.available()
            val bytesArray = ByteArray(size)
            inputStream.read(bytesArray)
            val jsonFile = String(bytesArray)
            val jsonObject = JSONObject(jsonFile)
            val organizationsNames = jsonObject.names()
            if (organizationsNames != null) {
                for (n in 0 until organizationsNames.length()) {
                    tempArray.add(organizationsNames.getString(n))
                }
            }
        } catch (e: IOException) { }
        return tempArray
    }

    //фильтруем названия районов, деревень, организация по вводимым буквам
    fun filterListData(list: ArrayList<String>, searchText: String?): ArrayList<String>{
        val tempList = ArrayList<String>()
        tempList.clear()
        if (searchText == null) {
            tempList.add("No result")
            return tempList
        }
        for (selection: String in list ){
            if (selection.lowercase().startsWith(searchText.lowercase()))
                tempList.add(selection)
        }
        if (tempList.size == 0) tempList.add("No result")
        return tempList
    }
}