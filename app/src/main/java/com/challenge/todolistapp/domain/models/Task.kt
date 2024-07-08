package com.challenge.todolistapp.domain.models

import java.util.Date
import java.util.UUID

data class Task(
    val id: Int = UUID.randomUUID().hashCode(),
    val title: String,
    val deadline: Date,
    var isCompleted: Boolean
) {
    val isOverdue: Boolean
        get() = !isCompleted && deadline.before(Date())
}
