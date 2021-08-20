package com.ncorti.kotlin.template.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskRow(
    task: Task,
    onTapBody: ((task: Task) -> Unit)? = null,
    onTapIsCompleted: ((task: Task) -> Unit)? = null
) {
    val icon = if (task.isCompleted)
        R.drawable.ic_baseline_radio_button_checked_24
        else R.drawable.ic_baseline_radio_button_unchecked_24

    Row(
        Modifier.padding(12.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null, // decorative element
            modifier = Modifier.clickable {
                onTapIsCompleted?.invoke(task)
            }
        )
        Text(
            task.body,
            Modifier.padding(start = 12.dp)
                .clickable { onTapBody?.invoke(task) }
        )
    }
}

@Preview
@Composable
fun PreviewTaskRow() {
    TaskRow(task = Task(body = "Get milk please", isCompleted = true))
}