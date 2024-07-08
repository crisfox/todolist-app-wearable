package com.challenge.todolistapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.challenge.todolistapp.presentation.viewmodel.TaskViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.challenge.todolistapp.presentation.screens.CreateTaskScreen
import com.challenge.todolistapp.presentation.screens.HomeScreen

@Composable
fun TodoTaskNav(viewModel: TaskViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "taskList") {
        composable("taskList") {
            HomeScreen(
                viewModel = viewModel,
                onAddTaskClick = { navController.navigate("createTask") }
            )
        }
        composable("createTask") {
            CreateTaskScreen(
                onTaskCreated = { navController.popBackStack() },
                onBackPressed = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}
