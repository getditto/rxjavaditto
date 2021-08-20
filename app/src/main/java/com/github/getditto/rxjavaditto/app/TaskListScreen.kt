package com.github.getditto.rxjavaditto.app

import androidx.compose.foundation.layout.Column
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.getditto.rxjavaditto.library.android.asObservable
import io.reactivex.rxjava3.core.Observable
import live.ditto.DittoDocumentID

class TasksListViewModel : ViewModel() {
    val tasks: Observable<List<Task>> = DittoManager.ditto.store["tasks"]
        .findAll()
        .asObservable()
        .map { snapshot -> snapshot.documents.map { doc -> Task(doc) } }

    fun toggleIsCompleted(task: Task) {
        DittoManager.ditto.store["tasks"]
            .findByID(DittoDocumentID(task._id))
            .update { mutableDoc ->
                mutableDoc?.let { mutableDoc ->
                    mutableDoc["isCompleted"].set(!mutableDoc["isCompleted"].booleanValue)
                }
            }
    }
}

@Composable
fun TaskListScreen(navController: NavController) {
    val viewModel = viewModel<TasksListViewModel>()
    val tasks: List<Task> by viewModel.tasks.subscribeAsState(initial = emptyList())
    Scaffold(
        content = {
            Column() {
                TopAppBar(
                    title = { Text(text = "AppBar") }
                )
                TaskList(
                    tasks = tasks,
                    toggleIsCompleted = { task -> viewModel.toggleIsCompleted(task) },
                    taskSelected = { selectedTask ->
                        navController.navigate(route = "tasks/${selectedTask._id}")
                    })
            }
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(route = "tasks/new")
                }
            ) {
                Icon(Icons.Filled.Add, "New Task")
            }
        })
}

@Composable
fun TaskList(
    tasks: List<Task>,
    toggleIsCompleted: ((task: Task) -> Unit)? = null,
    taskSelected: ((task: Task) -> Unit)? = null
) {
    Column() {
        tasks.forEach { task ->
            TaskRow(
                task = task,
                onTapIsCompleted = { t -> toggleIsCompleted?.invoke(t) },
                onTapBody = { t -> taskSelected?.invoke(t) })
        }
    }
}

@Preview(device = Devices.PIXEL_3, showSystemUi = true)
@Composable
fun PreviewTaskList() {
    val navController = rememberNavController()
    MaterialTheme() {
        TaskList(
            listOf(
                Task(body = "Do something", isCompleted = true),
                Task(body = "Lets eat!", isCompleted = false),
                Task(body = "Okay!", isCompleted = true),
            )
        )
    }
}