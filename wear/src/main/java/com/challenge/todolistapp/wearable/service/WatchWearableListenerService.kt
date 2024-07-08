package com.challenge.todolistapp.wearable.service

import android.content.Intent
import android.util.Log
import com.challenge.todolistapp.wearable.service.TaskBroadcastReceiver.Companion.ACTION_TASKS_UPDATED
import com.challenge.todolistapp.wearable.service.TaskBroadcastReceiver.Companion.EXTRA_TASKS_JSON
import com.challenge.todolistapp.wearable.utils.KeyType
import com.challenge.todolistapp.wearable.utils.PathType
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService


class WatchWearableListenerService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d(TAG, "onMessageReceived with WearableListenerService: $messageEvent")
        if (messageEvent.path == PathType.TASKS_RESPONSE.path) {
            val tasksJson = String(messageEvent.data)
            updateTasks(tasksJson)
            Log.d(TAG, "Received tasks with WearableListenerService: $tasksJson")
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d(TAG, "onDataChanged with WearableListenerService: $dataEvents")
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                if (dataItem.uri.path == PathType.TASKS_RESPONSE.path) {
                    val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                    val tasksJson = dataMap.getString(KeyType.TASKS.lowercase())
                    tasksJson?.let {
                        updateTasks(it)
                    }
                    Log.d(TAG, "Received tasks with WearableListenerService: $tasksJson")
                }
            }
        }
    }

    private fun updateTasks(tasksJson: String) {
        val intent = Intent(ACTION_TASKS_UPDATED).apply {
            putExtra(EXTRA_TASKS_JSON, tasksJson)
        }
        sendBroadcast(intent)
    }

    companion object {
        private const val TAG = "WatchWearableListenerService"
    }
}