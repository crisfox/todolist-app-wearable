package com.challenge.todolistapp.wearable.service

import android.content.Intent
import com.challenge.todolistapp.data.local.AppDatabase
import com.challenge.todolistapp.data.local.TaskEntity
import com.challenge.todolistapp.data.repository.SyncRepository
import com.challenge.todolistapp.domain.models.Task
import com.challenge.todolistapp.presentation.utils.TaskBroadcastReceiver.Companion.ACTION_TASK_UPDATED
import com.challenge.todolistapp.presentation.utils.TaskBroadcastReceiver.Companion.EXTRA_TASK_JSON
import com.challenge.todolistapp.wearable.utils.KeyType
import com.challenge.todolistapp.wearable.utils.PathType
import com.challenge.todolistapp.wearable.utils.deserializeTask
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.runBlocking
import java.util.Date

class PhoneWearableListenerService : WearableListenerService() {

    private val syncRepository = SyncRepository(this)
    private val database = AppDatabase.getDatabase(this)

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        if (messageEvent.path == PathType.REQUEST_TASKS.path) {
            val tasks = runBlocking { getTasksFromDatabase() }
            syncRepository.sendTasksToWearableMessage(messageEvent.sourceNodeId, tasks)
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)

        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem

                if (dataItem.uri.path == PathType.UPDATE_TASK.path) {
                    val data = DataMapItem.fromDataItem(dataItem).dataMap
                    val taskJson = data.getString(KeyType.TASK.lowercase())
                    val task = taskJson?.let { deserializeTask(it) }
                    runBlocking {
                        task?.let {
                            updateTaskInDatabase(it).also { updateTasks(taskJson) }
                        }
                    }
                }
            }
        }
    }

    private fun updateTasks(tasksJson: String) {
        val intent = Intent(ACTION_TASK_UPDATED).apply {
            putExtra(EXTRA_TASK_JSON, tasksJson)
        }
        sendBroadcast(intent)
    }

    private suspend fun getTasksFromDatabase(): List<Task> {
        return database.taskDao().getAllTasks()
            .map { Task(it.id, it.title, Date(it.deadline), it.isCompleted) }
    }

    private suspend fun updateTaskInDatabase(task: Task) {
        database.taskDao()
            .update(TaskEntity(task.id, task.title, task.deadline.time, task.isCompleted))
    }
}
