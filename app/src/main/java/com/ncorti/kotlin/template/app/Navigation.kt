package com.ncorti.kotlin.template.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "tasks") {
        composable(route = "tasks") {
            TaskListScreen(navController = navController)
        }
        composable(route = "tasks/new") {
            EditScreen(
                navController = navController,
                taskId = null
            )
        }
        composable(route = "tasks/{taskId}") { backStackEntry ->
            EditScreen(
                navController = navController,
                taskId = backStackEntry.arguments?.getString("taskId")
            )
        }
    }
}