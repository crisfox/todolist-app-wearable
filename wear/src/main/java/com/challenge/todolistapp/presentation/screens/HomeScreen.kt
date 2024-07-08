package com.challenge.todolistapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.challenge.todolistapp.presentation.ui.TaskItem
import com.challenge.todolistapp.presentation.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val tasks = viewModel.tasks.collectAsState().value

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) }
    ) {
        if (tasks.isNotEmpty()) {
            val listState = rememberScalingLazyListState()
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                autoCentering = AutoCenteringParams(itemIndex = 0),
                state = listState,
                flingBehavior = ScalingLazyColumnDefaults.snapFlingBehavior(
                    state = listState,
                )
            ) {
                val contentModifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)

                items(tasks.size) { index ->
                    val task = tasks[index]
                    TaskItem(modifier = contentModifier, task = task, onCompleted = {
                        viewModel.toggleCompletionTask(it)
                    })
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No tasks found")
                Button(
                    onClick = { viewModel.requestTaskList() }
                ) {
                    Text(text = "Actualizar")
                }
            }

        }
    }

}

