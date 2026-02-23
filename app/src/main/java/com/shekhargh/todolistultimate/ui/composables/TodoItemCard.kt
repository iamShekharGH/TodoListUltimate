package com.shekhargh.todolistultimate.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.util.dummyTasks
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TodoItemCard(
    todoTaskItem: TodoTaskItem,
    onNavigateToTask: (Int) -> Unit,
    onUpdateCompletionStatue: (Int, Boolean) -> Unit
) {
    val isDone = todoTaskItem.isItDone

    val cardContainerColor by animateColorAsState(
        targetValue = if (isDone) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surfaceVariant,
        label = "Card container color"
    )

    Card(
        onClick = { onNavigateToTask(todoTaskItem.id) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 4.dp, end = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardContainerColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (isDone) 1.dp else 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isDone,
                onCheckedChange = { onUpdateCompletionStatue(todoTaskItem.id, it) })
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = todoTaskItem.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (isDone) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = todoTaskItem.priority.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDone) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary

                    )
                }
                if (todoTaskItem.description.isNotEmpty()) {
                    Text(
                        text = todoTaskItem.description,
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (isDone) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                Text(
                    text = "Due: ${todoTaskItem.dueDate.format(formatter)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDone) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outline
                )
                if (todoTaskItem.tags.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        todoTaskItem.tags.forEach { tag ->
                            Surface(
                                modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = "#$tag",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(4.dp),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TodoItemCardPreview() {
    Column {
        TodoItemCard(dummyTasks[2], onNavigateToTask = {}, onUpdateCompletionStatue = { _, _ -> })
        Spacer(modifier = Modifier.size(16.dp))
        TodoItemCard(
            dummyTasks[2].copy(isItDone = true),
            onNavigateToTask = {},
            onUpdateCompletionStatue = { _, _ -> })
    }

}
