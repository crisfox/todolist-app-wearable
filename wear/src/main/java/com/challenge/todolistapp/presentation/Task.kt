package com.challenge.todolistapp.presentation

import java.util.Date

data class Task(val id: Long, val title: String, val deadline: Date, var isCompleted: Boolean) {
    val isOverdue: Boolean
        get() = !isCompleted && deadline.before(Date())
}
