package com.bangkit.heyfo

import android.content.Context
import com.bangkit.heyfo.data.response.DataItemPredict
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferences {

    fun savePredictList(context: Context, predictList: List<DataItemPredict>) {
        val sharedPreferences = context.getSharedPreferences("PREDICT_PREF", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val jsonString = Gson().toJson(predictList)
        editor.putString("PREDICT_LIST_JSON", jsonString)
        editor.apply()
    }

    fun loadPredictList(context: Context): List<DataItemPredict> {
        val sharedPreferences = context.getSharedPreferences("PREDICT_PREF", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("PREDICT_LIST_JSON", null)
        return if (jsonString != null) {
            val type = object : TypeToken<List<DataItemPredict>>() {}.type
            Gson().fromJson(jsonString, type)
        } else {
            listOf()
        }
    }
}
