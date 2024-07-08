package com.challenge.todolistapp.notifications.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast

fun requestExactAlarmPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Exact alarm scheduling is not supported on this version of Android.", Toast.LENGTH_LONG).show()
    }
}

fun canScheduleExactAlarms(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }
}

fun scheduleExactAlarm(context: Context, deadlineMillis: Long, taskId: Long) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!canScheduleExactAlarms(context)) {
            requestExactAlarmPermission(context)
            return
        }
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("TASK_ID", taskId)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        taskId.toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    try {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, deadlineMillis, pendingIntent)
    } catch (e: SecurityException) {
        Toast.makeText(context, "Permission required to schedule exact alarms.", Toast.LENGTH_LONG).show()
    }
}