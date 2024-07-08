package com.challenge.todolistapp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.wear.compose.material.Checkbox
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.ToggleChipDefaults
import com.challenge.todolistapp.presentation.Task


@Composable
fun TaskItem(modifier: Modifier = Modifier, task: Task, onCompleted: (Task) -> Unit) {
    var checked by remember { mutableStateOf(task.isCompleted) }

    LaunchedEffect(checked) {
        if (checked != task.isCompleted) {
            onCompleted(task.copy(isCompleted = checked))
        }
    }

    ToggleChip(
        modifier = modifier,
        label = { Text(task.title) },
        checked = checked,
        onCheckedChange = { },
        enabled = !task.isOverdue && !task.isCompleted,
        colors = ToggleChipDefaults.toggleChipColors(
            uncheckedToggleControlColor = ToggleChipDefaults.SwitchUncheckedIconColor
        ),
        toggleControl = {
            Checkbox(
                checked = checked,
                enabled = !task.isOverdue && !task.isCompleted,
                onCheckedChange = {
                    checked = it
                }
            )
        },
        secondaryLabel = {
            Text(
                text = if (task.isCompleted) "Completada" else if (task.isOverdue) "Vencida" else "Pendiente",
                color = if (task.isCompleted) MaterialTheme.colors.primary
                else if (task.isOverdue) MaterialTheme.colors.error
                else MaterialTheme.colors.secondary
            )
        }
    )
}
