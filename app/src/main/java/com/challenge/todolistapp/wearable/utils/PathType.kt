package com.challenge.todolistapp.wearable.utils

enum class PathType(val path: String) {
    REQUEST_TASKS("/request_tasks"),
    UPDATE_TASK("/update_task"),
    TASKS_RESPONSE("/tasks_response"),
}

enum class KeyType {
    TASK,
    TASKS;

    fun lowercase() : String {
        return this.name.lowercase()
    }
}
