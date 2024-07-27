package com.mtt.weatherapp.common

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

object Utils {
    fun loadJsonFromRaw(context: Context, rawResourceId: Int): JSONObject {
        val inputStream = context.resources.openRawResource(rawResourceId)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val jsonString = bufferedReader.use { it.readText() }
        return JSONObject(jsonString)
    }

    fun getImageUrl(jsonObject: JSONObject, key: String): String? {
        return jsonObject.optJSONObject(key)?.optString("image")
    }

}