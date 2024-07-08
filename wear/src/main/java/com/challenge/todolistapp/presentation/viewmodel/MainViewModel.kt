package com.challenge.todolistapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.challenge.todolistapp.presentation.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val application: Application, private val repository: SyncRepository) : AndroidViewModel(application) {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> get() = _tasks


    init {
        requestTaskList()
    }

    fun requestTaskList() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            repository.requestTasksFromPhone(application)
        }
    }

    fun updateTasks(tasks: List<Task>) {
        _tasks.value = tasks
    }

    fun toggleCompletionTask(task: Task) {
        viewModelScope.launch {
            _tasks.value = _tasks.value.map { if (it.id == task.id) task else it }
            repository.sendUpdateTask(task)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY]!!
                MainViewModel(
                    application,
                    SyncRepository(application)
                )
            }
        }
    }

}
