package com.challenge.todolistapp

import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.challenge.todolistapp.data.local.AppDatabase
import com.challenge.todolistapp.data.repository.SyncRepository
import com.challenge.todolistapp.data.repository.TaskRepository
import com.challenge.todolistapp.notifications.checkNotificationSettings
import com.challenge.todolistapp.presentation.navigation.TodoTaskNav
import com.challenge.todolistapp.presentation.utils.TaskBroadcastReceiver
import com.challenge.todolistapp.presentation.utils.TaskBroadcastReceiver.Companion.ACTION_TASK_UPDATED
import com.challenge.todolistapp.presentation.viewmodel.TaskViewModel
import com.challenge.todolistapp.presentation.viewmodel.TaskViewModelFactory
import com.challenge.todolistapp.ui.theme.TodoListAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var taskViewModel: TaskViewModel

    private lateinit var taskBroadcastReceiver: TaskBroadcastReceiver

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(ACTION_TASK_UPDATED)
        registerReceiver(taskBroadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(taskBroadcastReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = AppDatabase.getDatabase(this)
        val repository = TaskRepository(db.taskDao())
        taskViewModel = ViewModelProvider(
            this,
            TaskViewModelFactory(application, repository, SyncRepository(this))
        )[TaskViewModel::class.java]

        taskBroadcastReceiver = TaskBroadcastReceiver {
            taskViewModel.updateTask(it)
        }

        setContent {
            TodoListAppTheme {
                TodoTaskNav(taskViewModel)
            }
        }

        // Initialize the permission launcher
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        // Request permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        // Check notification settings
        checkNotificationSettings(this)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoListAppTheme {
        TodoTaskNav()
    }
}
