package com.challenge.todolistapp.wearable.utils

import com.challenge.todolistapp.presentation.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun serializeTask(task: Task): String {
    val gson = Gson()
    return gson.toJson(task)
}

fun deserializeTasks(tasksJson: String): List<Task> {
    return try {
        val gson = Gson()
        val taskType = object : TypeToken<List<Task>>() {}.type
        gson.fromJson(tasksJson, taskType)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
