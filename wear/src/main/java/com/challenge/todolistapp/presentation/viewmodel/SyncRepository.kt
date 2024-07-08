package com.challenge.todolistapp.presentation.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.challenge.todolistapp.presentation.Task
import com.challenge.todolistapp.wearable.utils.KeyType
import com.challenge.todolistapp.wearable.utils.PathType
import com.challenge.todolistapp.wearable.utils.serializeTask
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class SyncRepository(private val context: Context) {
    private val dataClient by lazy { Wearable.getDataClient(context) }

    fun requestTasksFromPhone(context: Context) {
        val nodeId = getNodeId(context)
        val messageClient = Wearable.getMessageClient(context)

        nodeId?.let { node ->
            messageClient.sendMessage(node, PathType.REQUEST_TASKS.path, ByteArray(0)).apply {
                addOnSuccessListener {
                    Log.d("RequestTasks", "Request sent to $node")
                }
                addOnFailureListener {
                    Log.e("RequestTasks", "Error sending request to $node", it)
                }
            }
        }
    }

    private fun getNodeId(context: Context): String? {
        val nodes = Tasks.await(Wearable.getNodeClient(context).connectedNodes)
        return nodes.firstOrNull()?.id
    }

    suspend fun sendUpdateTask(task: Task) {
        try {
            val request = PutDataMapRequest.create(PathType.UPDATE_TASK.path).apply {
                dataMap.putString(KeyType.TASK.lowercase(), serializeTask(task))
            }
                .asPutDataRequest()
                .setUrgent()

            val result = dataClient.putDataItem(request).await()

            Log.d(ContentValues.TAG, "DataItem saved: $result")
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            Log.d(ContentValues.TAG, "Saving DataItem failed: $exception")
        }
    }
}
