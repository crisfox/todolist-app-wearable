package com.challenge.todolistapp.presentation.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.challenge.todolistapp.domain.models.Task
import com.challenge.todolistapp.wearable.utils.deserializeTask

class TaskBroadcastReceiver(private val onTaskUpdated: (Task) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (intent.action == ACTION_TASK_UPDATED) {
                val tasksJson = intent.getStringExtra(EXTRA_TASK_JSON)
                val tasks = tasksJson?.let { deserializeTask(it) }
                tasks?.let { onTaskUpdated(it) }
            }
        }
    }

    companion object {
        const val ACTION_TASK_UPDATED = "TASK_UPDATED"
        const val EXTRA_TASK_JSON = "extra_task_json"
    }
}
