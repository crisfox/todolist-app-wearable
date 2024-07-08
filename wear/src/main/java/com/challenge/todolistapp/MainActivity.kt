package com.challenge.todolistapp

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.TimeText
import com.challenge.todolistapp.presentation.screens.HomeScreen
import com.challenge.todolistapp.presentation.viewmodel.MainViewModel
import com.challenge.todolistapp.presentation.viewmodel.MainViewModelFactory
import com.challenge.todolistapp.presentation.viewmodel.SyncRepository
import com.challenge.todolistapp.theme.TodoListAppTheme
import com.challenge.todolistapp.wearable.service.TaskBroadcastReceiver
import com.challenge.todolistapp.wearable.service.TaskBroadcastReceiver.Companion.ACTION_TASKS_UPDATED

class MainActivity : ComponentActivity() {

    private lateinit var mainViewModel : MainViewModel

    private lateinit var taskBroadcastReceiver : BroadcastReceiver

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(ACTION_TASKS_UPDATED)
        registerReceiver(taskBroadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(taskBroadcastReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(application, SyncRepository(this))
        )[MainViewModel::class.java]

        taskBroadcastReceiver = TaskBroadcastReceiver {
            mainViewModel.updateTasks(it)
        }

        setContent {
            WearApp(mainViewModel)
        }
    }

}

@Composable
fun WearApp(mainViewModel: MainViewModel) {
    TodoListAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            HomeScreen(mainViewModel)
        }
    }
}
