package com.challenge.todolistapp.wearable.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.challenge.todolistapp.presentation.Task
import com.challenge.todolistapp.wearable.utils.deserializeTasks

class TaskBroadcastReceiver(private val onTaskUpdated: (List<Task>) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TaskBroadcastReceiver", "onReceive")
        intent?.let {
            if (intent.action == ACTION_TASKS_UPDATED) {
                val tasksJson = intent.getStringExtra(EXTRA_TASKS_JSON)
                val tasks = tasksJson?.let { deserializeTasks(it) }
                tasks?.let { onTaskUpdated(it) }
            }
        }
    }

    companion object {
        const val ACTION_TASKS_UPDATED = "TASKS_UPDATED"
        const val EXTRA_TASKS_JSON = "extra_tasks_json"
    }
}