package com.shekhargh.todolistultimate.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    onNavigateBack: () -> Unit,
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
            TopAppBar(
                title = {
                    Text(text = "Add/Edit Tasks.")

                },
                actions = {
                    IconButton(onClick = {
                        viewModel.onDeleteClicked(onNavigateBack)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete the task."
                        )
                    }
                }
            )

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
                onDateTimeChanged = viewModel::onDateTimeChanged,
                onNavigateBack = onNavigateBack
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreenComposable(
    uiState: State<AddTaskUiState>,
    onTitleChanges: (String) -> Unit,
    onDescriptionChanges: (String) -> Unit,
    onDoneCheck: (Boolean) -> Unit,
    onPrioritySelected: (Priority) -> Unit,
    onSubmitClicked: (onNavigateBack: () -> Unit) -> Unit,
    onDateTimeChanged: (LocalDateTime) -> Unit,
    onNavigateBack: () -> Unit
) {

    var showPriorityMenu by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.value.dueDate.atZone(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
    )
    val timePickerState = rememberTimePickerState(
        initialHour = uiState.value.dueDate.hour,
        initialMinute = uiState.value.dueDate.minute
    )



    Column {

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
                    uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            )
        }
        HorizontalDivider(Modifier.padding(start = 8.dp, end = 8.dp))

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
                    showDatePicker = true
                }
            ) {
                Text(text = uiState.value.dueDate.toSimpleDateString())
            }
        }
        HorizontalDivider(Modifier.padding(start = 8.dp, end = 8.dp))

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
                    showTimePicker = true
                }
            ) {
                Text(text = uiState.value.dueDate.toSimpleTimeString())
            }
        }
        HorizontalDivider(Modifier.padding(start = 8.dp, end = 8.dp))

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

        HorizontalDivider(Modifier.padding(start = 8.dp, end = 8.dp))


        OutlinedButton(
            onClick = {
                onSubmitClicked(onNavigateBack)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text("Submit Task")
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val newDate =
                                    Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                val currentDate = uiState.value.dueDate
                                onDateTimeChanged(newDate.atTime(currentDate.toLocalTime()))
                                showDatePicker = false
                            }
                        }
                    ) {
                        Text("Ok")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }

        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                title = { Text("When is it Due?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val newTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                            val currentDateTime = uiState.value.dueDate
                            onDateTimeChanged(newTime.atDate(currentDateTime.toLocalDate()))
                            showTimePicker = false
                        }
                    ) {
                        Text("Ok")
                    }
                }
            ) {
                TimePicker(state = timePickerState)
            }
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
        onDateTimeChanged = {},
        onNavigateBack = {}
    )
}
