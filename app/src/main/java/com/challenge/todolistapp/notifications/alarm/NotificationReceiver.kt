package com.challenge.todolistapp.notifications.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.challenge.todolistapp.data.local.AppDatabase
import com.challenge.todolistapp.notifications.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra("TASK_ID", -1)
        if (taskId == -1L) {
            Toast.makeText(context, "Invalid task ID", Toast.LENGTH_SHORT).show()
            return
        }

        val notificationHelper = NotificationHelper(context)
        val taskDao = AppDatabase.getDatabase(context).taskDao()

        runBlocking {
            val task = withContext(Dispatchers.IO) {
                taskDao.getTaskById(taskId.toInt())
            }

            if (task != null && !task.isCompleted) {
                notificationHelper.showNotification(
                    "Tarea expirada",
                    "No llegaste a completar la siguiente tarea: '${task.title}'"
                )
            }
        }
    }
}
