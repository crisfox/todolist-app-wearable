package com.challenge.todolistapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.todolistapp.data.local.TaskEntity
import com.challenge.todolistapp.data.repository.SyncRepository
import com.challenge.todolistapp.data.repository.TaskRepository
import com.challenge.todolistapp.domain.models.Task
import com.challenge.todolistapp.notifications.alarm.scheduleExactAlarm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class TaskViewModel(
    private val application: Application,
    private val repository: TaskRepository,
    private val syncRepository: SyncRepository
) :
    AndroidViewModel(application) {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> get() = _tasks

    private var isSortedByDate = false

    init {
        syncWearableData()
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = repository.getAllTasks()
                .map { Task(it.id, it.title, Date(it.deadline), it.isCompleted) }
                .sortedByDescending { it.deadline }
        }
    }

    private fun syncWearableData() {
        viewModelScope.launch {
            _tasks.collect { tasks ->
                syncRepository.sendTasksToWearable(tasks)
            }
        }
    }

    fun addTask(title: String, deadline: Date? = null) {
        val deadlineCheck = deadline ?: Date(System.currentTimeMillis() + 86400000)
        val task = Task(
            title = title,
            deadline = deadlineCheck,
            isCompleted = false
        )
        _tasks.value += task
        viewModelScope.launch {
            repository.insert(
                TaskEntity(
                    id = task.id,
                    title = title,
                    deadline = deadlineCheck.time,
                    isCompleted = false
                )
            )
            scheduleExactAlarm(application, deadlineCheck.time, task.id.toLong())
        }
    }

    fun removeTask(task: Task) {
        _tasks.value -= task
        viewModelScope.launch {
            repository.delete(task.id)
        }
    }

    fun updateTask(task: Task) {
        _tasks.value = _tasks.value.map { if (it.id == task.id) task else it }
        viewModelScope.launch {
            repository.update(
                TaskEntity(
                    id = task.id,
                    title = task.title,
                    deadline = task.deadline.time,
                    isCompleted = task.isCompleted
                )
            )
        }
    }

    fun toggleSortByDate() {
        isSortedByDate = !isSortedByDate
        _tasks.value = if (isSortedByDate) {
            _tasks.value.sortedByDescending { it.deadline }
        } else {
            _tasks.value.sortedBy { it.deadline }
        }
    }
}
