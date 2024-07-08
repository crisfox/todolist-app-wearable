package com.challenge.todolistapp.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.challenge.todolistapp.domain.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TaskCard(task: Task, onCompleted: (Task) -> Unit) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    var checked by remember { mutableStateOf(task.isCompleted) }

    LaunchedEffect(checked) {
        if (checked != task.isCompleted) {
            onCompleted(task.copy(isCompleted = checked))
        }
    }

    ListItem(
        modifier = Modifier.clip(MaterialTheme.shapes.small),

        headlineContent = {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
        },
        supportingContent = {
            Text(
                text = dateFormat.format(task.deadline),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingContent = {
            AssistChip(
                onClick = { },
                enabled = !task.isOverdue && !task.isCompleted,
                label = {
                    Text(
                        text = if (task.isCompleted) "Completada" else if (task.isOverdue) "Vencida" else "Pendiente",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.primary
                        else if (task.isOverdue) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.secondary
                    )
                },
                leadingIcon = {
                    if (task.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Completada"
                        )
                    } else if (task.isOverdue) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Vencida"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Pendiente"
                        )
                    }
                }
            )

        },
        leadingContent = {
            Checkbox(
                enabled = !task.isOverdue && !task.isCompleted,
                checked = checked,
                onCheckedChange = {
                    checked = it
                }
            )
        })

}
