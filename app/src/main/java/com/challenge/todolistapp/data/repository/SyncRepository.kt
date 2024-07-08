package com.challenge.todolistapp.data.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.challenge.todolistapp.domain.models.Task
import com.challenge.todolistapp.wearable.utils.KeyType
import com.challenge.todolistapp.wearable.utils.PathType
import com.challenge.todolistapp.wearable.utils.serializeTasks
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import kotlin.coroutines.cancellation.CancellationException

class SyncRepository(private val context: Context) {

    private val dataClient by lazy { Wearable.getDataClient(context) }
    private val messageClient by lazy { Wearable.getMessageClient(context) }

    fun sendTasksToWearableMessage(nodeId: String, tasks: List<Task>) {
        val gson = Gson()
        val data = gson.toJson(tasks).toByteArray()

        nodeId.let {
            messageClient.sendMessage(it, PathType.TASKS_RESPONSE.path, data).apply {
                addOnSuccessListener {
                    Log.d("WearableData with sendMessage", "Data sent successfully")
                }
                addOnFailureListener {
                    Log.e("WearableData wit sendMessage", "Failed to send data")
                }
            }
        }
    }

    fun sendTasksToWearable(tasks: List<Task>) {
        try {
            val request = PutDataMapRequest.create(PathType.TASKS_RESPONSE.path).apply {
                dataMap.putString(KeyType.TASKS.lowercase(), serializeTasks(tasks))
            }
                .asPutDataRequest()
                .setUrgent()

            dataClient.putDataItem(request).addOnSuccessListener {
                Log.d(TAG, "DataItem sent")
            }.addOnFailureListener {
                Log.d(TAG, "Sending DataItem failed: $it")
            }
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            Log.d(TAG, "Saving DataItem failed: $exception")
        }
    }
}
