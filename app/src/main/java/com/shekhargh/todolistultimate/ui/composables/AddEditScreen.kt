package com.shekhargh.todolistultimate.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shekhargh.todolistultimate.data.Priority
import com.shekhargh.todolistultimate.ui.viewModels.AddTaskUiState
import com.shekhargh.todolistultimate.ui.viewModels.AddTaskViewModel
import com.shekhargh.todolistultimate.ui.viewModels.toAddTaskUiState
import com.shekhargh.todolistultimate.util.dummyTasks
import com.shekhargh.todolistultimate.util.toSimpleDateString
import com.shekhargh.todolistultimate.util.toSimpleTimeString
import java.time.format.DateTimeFormatter


@Composable
fun AddEditScreen(
    viewModel: AddTaskViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {

        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {

            AddEditScreenComposable(
                uiState = uiState,
                onTitleChanges = viewModel::onTitleChanges,
                onDescriptionChanges = viewModel::onDescriptionChanges,
                onDoneCheck = viewModel::onDoneCheck,
                onPrioritySelected = viewModel::onPrioritySelected,
                onSubmitClicked = viewModel::onSubmitClicked,
                paddingValues = padding
            )
        }
    }
}

@Composable
fun AddEditScreenComposable(
    uiState: State<AddTaskUiState>,
    onTitleChanges: (String) -> Unit,
    onDescriptionChanges: (String) -> Unit,
    onDoneCheck: (Boolean) -> Unit,
    onPrioritySelected: (Priority) -> Unit,
    onSubmitClicked: () -> Unit,
    paddingValues: PaddingValues
) {

    var showPriorityMenu by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.padding(paddingValues)
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Title")
            },
            value = uiState.value.title,
            onValueChange = onTitleChanges,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Description")
            },
            value = uiState.value.description,
            onValueChange = onDescriptionChanges,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Task Status", fontSize = 19.sp)
            Switch(
                checked = uiState.value.isItDone,
                onCheckedChange = onDoneCheck,
                thumbContent = {
                    if (uiState.value.isItDone) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Task Done",
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Block,
                            contentDescription = "Task Not Done",
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.error,
                    uncheckedTrackColor = MaterialTheme.colorScheme.errorContainer,
                )
            )
        }
        HorizontalDivider(
            Modifier.padding(start = 8.dp, end = 8.dp),
            DividerDefaults.Thickness,
            DividerDefaults.color
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "On", fontSize = 19.sp)
            ElevatedButton(
                onClick = {

                }
            ) {
                Text(text = uiState.value.dueDate.toSimpleDateString())
            }
        }
        HorizontalDivider(
            Modifier.padding(start = 8.dp, end = 8.dp),
            DividerDefaults.Thickness,
            DividerDefaults.color
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "At", fontSize = 19.sp)
            ElevatedButton(
                onClick = {

                }
            ) {
                Text(text = uiState.value.dueDate.toSimpleTimeString())
            }
        }
        HorizontalDivider(
            Modifier.padding(start = 8.dp, end = 8.dp),
            DividerDefaults.Thickness,
            DividerDefaults.color
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Priority", fontSize = 19.sp, modifier = Modifier.weight(1f))

            Box(
                contentAlignment = Alignment.BottomEnd,

                ) {
                ElevatedButton(
                    onClick = {
                        showPriorityMenu = true
                    }
                ) {
                    Text(text = uiState.value.priority.name)
                }

                DropdownMenu(
                    expanded = showPriorityMenu,
                    onDismissRequest = { showPriorityMenu = false }
                ) {
                    Priority.entries.forEach { priority ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = priority.name
                                )
                            },
                            onClick = {
                                onPrioritySelected(priority)
                                showPriorityMenu = false
                            }
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            Modifier.padding(start = 8.dp, end = 8.dp),
            DividerDefaults.Thickness,
            DividerDefaults.color
        )


        OutlinedButton(
            onClick = {
                onSubmitClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text("Submit Task")
        }
    }
}

@Preview
@Composable
fun AddEditScreenComposablePreview() {
    val dummyItem = dummyTasks[11].toAddTaskUiState()
    AddEditScreenComposable(
        uiState = remember { mutableStateOf(dummyItem) },
        onTitleChanges = { },
        onDescriptionChanges = {},
        onDoneCheck = {},
        onPrioritySelected = {},
        onSubmitClicked = {},
        paddingValues = PaddingValues(4.dp)
    )
}