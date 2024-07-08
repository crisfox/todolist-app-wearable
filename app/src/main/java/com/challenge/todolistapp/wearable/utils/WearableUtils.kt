package com.challenge.todolistapp.wearable.utils

import com.challenge.todolistapp.domain.models.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun serializeTasks(tasks: List<Task>): String {
    val gson = Gson()
    return gson.toJson(tasks)
}

fun deserializeTask(taskData: String): Task? {
    return try {
        val gson = Gson()
        val taskType = object : TypeToken<Task>() {}.type
        gson.fromJson(taskData, taskType)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
