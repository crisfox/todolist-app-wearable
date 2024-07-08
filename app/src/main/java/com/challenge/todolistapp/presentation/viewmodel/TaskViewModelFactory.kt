package com.challenge.todolistapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.challenge.todolistapp.data.repository.SyncRepository
import com.challenge.todolistapp.data.repository.TaskRepository

class TaskViewModelFactory(private val application: Application, private val repository: TaskRepository, private val syncRepository: SyncRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(application, repository, syncRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
