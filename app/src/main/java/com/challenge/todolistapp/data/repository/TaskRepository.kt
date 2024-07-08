package com.challenge.todolistapp.data.repository

import com.challenge.todolistapp.data.local.TaskDao
import com.challenge.todolistapp.data.local.TaskEntity

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun insert(task: TaskEntity) {
        taskDao.insert(task)
    }

    suspend fun update(task: TaskEntity) {
        taskDao.update(task)
    }

    suspend fun delete(taskId: Int) {
        taskDao.delete(taskId)
    }

    suspend fun getAllTasks(): List<TaskEntity> {
        return taskDao.getAllTasks()
    }
}
