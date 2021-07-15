package ru.rayanis.stroyka.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException

object VillageHelper {
    fun getAllAreas(context: Context): ArrayList<String> {
        var tempArray = ArrayList<String>()
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

        } catch (e: IOException) {

        }
        return tempArray
    }
}