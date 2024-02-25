package com.example.practica01.objects

import com.example.practica01.models.Alarm
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

object State : Serializable {
    var alarms : ArrayList<Alarm> = ArrayList()

    fun addAlarm(newAlarm : Alarm) {
        this.alarms.add(newAlarm)
    }

    fun toJson(): String {
        return Gson().toJson(alarms)
    }

    fun fromJson(json : String) {
        val typeToken = object : TypeToken<List<Alarm>>() {}.type
        var newAlarms = Gson().fromJson<List<Alarm>>(json, typeToken)
        alarms = newAlarms.toCollection(ArrayList())
    }
}