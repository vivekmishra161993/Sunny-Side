package com.mtt.weatherapp.common

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.coroutines.resume

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

    @SuppressLint("MissingPermission")
    suspend fun FusedLocationProviderClient.awaitLastLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    continuation.resume(task.result)
                } else {
                    continuation.resume(null)
                }
            }
        }


}