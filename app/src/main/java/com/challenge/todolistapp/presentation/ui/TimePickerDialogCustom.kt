package com.challenge.todolistapp.presentation.ui

import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun TimePickerDialogCustom(onTimeSelected: (Int, Int) -> Unit, onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val timePicker = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            onTimeSelected(hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    timePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel") { _, _ ->
        onDismissRequest()
    }

    DisposableEffect(Unit) {
        timePicker.show()
        onDispose {
            timePicker.dismiss()
        }
    }
}
