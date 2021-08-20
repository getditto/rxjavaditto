package com.ncorti.kotlin.template.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.livedata.observeAsState
import live.ditto.DittoDocumentID

class EditScreenViewModel : ViewModel() {

    private var taskId: String? = null;

    private val _body = MutableLiveData("")
    val body: LiveData<String> = _body
    private val _isCompleted = MutableLiveData(false)
    val isCompleted: LiveData<Boolean> = _isCompleted
    fun onIsCompletedChange(isCompleted: Boolean) {
        _isCompleted.value = isCompleted
    }
    fun onBodyChange(text: String) {
        _body.value = text;
    }
    fun setTaskId(taskId: String? = null) {
        this.taskId = taskId;
        taskId?.let { taskId ->
            DittoManager.ditto.store["tasks"]
                .findByID(DittoDocumentID(taskId))
                .exec()?.let { doc ->
                    val task = Task(doc)
                    _body.value = task.body
                    _isCompleted.value = task.isCompleted
                }
        }
    }
    fun save() {
        if (this.taskId == null) {
            DittoManager.ditto.store["tasks"].insert(mapOf(
                "body" to this.body.value!!,
                "isCompleted" to this.isCompleted.value!!
            ))
        } else {
            DittoManager.ditto.store["tasks"]
                .findByID(DittoDocumentID(taskId!!))
                .update { mutableDoc ->
                    mutableDoc?.let { mutableDoc ->
                        mutableDoc["body"].set(this.body.value!!)
                        mutableDoc["isCompleted"].set(this.isCompleted.value!!)
                    }
                }
        }
    }
}

@Composable
fun EditScreen(navController: NavController, taskId: String? = null) {
    val viewModel = viewModel<EditScreenViewModel>()
    viewModel.setTaskId(taskId);
    val body = viewModel.body.observeAsState(initial = "")
    val isCompleted = viewModel.isCompleted.observeAsState(initial = false)
    EditScreenForm(
        body = body.value,
        onBodyChange = { viewModel.onBodyChange(it) },
        isCompleted = isCompleted.value,
        onIsCompleteChange = { viewModel.onIsCompletedChange(it) },
        saveButtonClicked = {
            viewModel.save()
            navController.popBackStack()
        }
    )
}

@Composable
fun EditScreenForm(
    body: String,
    onBodyChange: (name: String) -> Unit,
    isCompleted: Boolean,
    onIsCompleteChange: (isCompleted: Boolean) -> Unit,
    saveButtonClicked: (() -> Unit)? = null
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Task:")
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = body,
            onValueChange = { text -> onBodyChange(text) })
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Switch(checked = isCompleted, onCheckedChange = {
                onIsCompleteChange(it)
            })
            Text(
                text = "Is Completed",
            )
        }
        Button(
            onClick = {
                saveButtonClicked?.invoke()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Save", textAlign = TextAlign.Center)
        }
    }
}


@Preview(device = Devices.PIXEL_3, showSystemUi = true)
@Composable
fun PreviewEditScreenForm() {


    MaterialTheme() {
        EditScreenForm(
            body = "Foo",
            onBodyChange = { },
            onIsCompleteChange = { },
            isCompleted = false,
        )
    }
}