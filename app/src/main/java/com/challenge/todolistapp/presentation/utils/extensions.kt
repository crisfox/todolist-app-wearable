package com.challenge.todolistapp.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toDateString(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.toTimeString(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(this)
}
