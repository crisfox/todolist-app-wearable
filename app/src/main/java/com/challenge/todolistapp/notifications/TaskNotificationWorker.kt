package com.challenge.todolistapp.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.challenge.todolistapp.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class TaskNotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        return try {
            // Obtain the NotificationHelper instance
            val notificationHelper = NotificationHelper(applicationContext)
            // Get the TaskDao instance
            val taskDao = AppDatabase.getDatabase(applicationContext).taskDao()

            // Use coroutines to perform database operations on IO thread
            runBlocking {
                val currentTime = System.currentTimeMillis()
                val tasks = withContext(Dispatchers.IO) {
                    taskDao.getAllTasks().filter {
                        !it.isCompleted && it.deadline <= currentTime
                    }
                }
                tasks.forEach { task ->
                    notificationHelper.showNotification(
                        "Task Overdue",
                        "Task '${task.title}' is overdue and has not been completed."
                    )
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
