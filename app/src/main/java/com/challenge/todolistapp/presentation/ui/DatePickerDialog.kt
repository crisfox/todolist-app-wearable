package com.challenge.todolistapp.presentation.ui

import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun DatePickerDialogCustom(onDateSelected: (Int, Int, Int) -> Unit, onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(year, month, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel") { _, _ ->
        onDismissRequest()
    }

    DisposableEffect(Unit) {
        datePicker.show()
        onDispose {
            datePicker.dismiss()
        }
    }
}
