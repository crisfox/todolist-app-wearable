package com.challenge.todolistapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.challenge.todolistapp.presentation.ui.DatePickerDialogCustom
import com.challenge.todolistapp.presentation.ui.TimePickerDialogCustom
import com.challenge.todolistapp.presentation.utils.toDateString
import com.challenge.todolistapp.presentation.utils.toTimeString
import com.challenge.todolistapp.presentation.viewmodel.TaskViewModel
import java.util.Calendar
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    onTaskCreated: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: TaskViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf(Calendar.getInstance().time) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        TopAppBar(title = { Text(text = "Crear nueva tarea") },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Título")
            TextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Fecha y hora del vencimiento")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { showDatePicker = true }) {
                    Text(text = deadline.toDateString())
                }
                Button(onClick = { showTimePicker = true }) {
                    Text(text = deadline.toTimeString())
                }
            }

            if (showDatePicker) {
                DatePickerDialogCustom(
                    onDateSelected = { year, month, dayOfMonth ->
                        val calendar = Calendar.getInstance().apply {
                            time = deadline
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        }
                        deadline = calendar.time
                        showDatePicker = false
                    },
                    onDismissRequest = { showDatePicker = false }
                )
            }

            if (showTimePicker) {
                TimePickerDialogCustom(
                    onTimeSelected = { hourOfDay, minute ->
                        val calendar = Calendar.getInstance().apply {
                            time = deadline
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }
                        deadline = calendar.time
                        showTimePicker = false
                    },
                    onDismissRequest = { showTimePicker = false }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(Color.White)
                .windowInsetsPadding(WindowInsets.ime)// Align button to bottom
        ) {
            Button(
                onClick = {
                    viewModel.addTask(
                        title = title,
                        deadline = deadline,
                    )
                    onTaskCreated()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Guardar Tarea")
            }
        }

    }

}

@Composable
@Preview(showBackground = true)
fun CreateTaskScreenPreview() {
    CreateTaskScreen(onTaskCreated = {}, onBackPressed = {})
}
