@file:OptIn(ExperimentalMaterial3Api::class)

package com.challenge.todolistapp.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.challenge.todolistapp.presentation.ui.SwipeToDeleteContainer
import com.challenge.todolistapp.presentation.ui.TaskCard
import com.challenge.todolistapp.presentation.viewmodel.TaskViewModel

@Composable
fun HomeScreen(viewModel: TaskViewModel = viewModel(), onAddTaskClick: () -> Unit) {
    val tasks by viewModel.tasks.collectAsState()
    var isSorted by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tareas") },
                actions = {
                    IconButton(onClick = {
                        viewModel.toggleSortByDate()
                        isSorted = !isSorted
                    }) {
                        Icon(
                            imageVector = if (isSorted) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = "Ordenar por fecha"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onAddTaskClick()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Tarea")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(tasks,
                key = { it.id }
            ) { item ->
                SwipeToDeleteContainer(item, onRemove = viewModel::removeTask) {
                    TaskCard(task = item, onCompleted = viewModel::updateTask)
                }
            }
        }
    }
}
