package com.shekhargh.todolistultimate.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    Text(text = if (uiState.value.id == 0) "Add Task" else "Edit Task")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (uiState.value.id != 0) {
                        IconButton(onClick = {
                            viewModel.onDeleteClicked(onNavigateBack)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete the task."
                            )
                        }
                    }
                }
            )

        }
    ) { padding ->
        AddEditScreenComposable(
            modifier = Modifier.padding(padding),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreenComposable(
    modifier: Modifier = Modifier,
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Title") },
            value = uiState.value.title,
            onValueChange = onTitleChanges,
            leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Description") },
            value = uiState.value.description,
            onValueChange = onDescriptionChanges,
            leadingIcon = { Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = null) },
            minLines = 3
        )

        HorizontalDivider()

        ListItem(
            headlineContent = { Text("Status") },
            supportingContent = { Text(if (uiState.value.isItDone) "Completed" else "Pending") },
            trailingContent = {
                Switch(
                    checked = uiState.value.isItDone,
                    onCheckedChange = onDoneCheck,
                    thumbContent = {
                        if (uiState.value.isItDone) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Block,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    }
                )
            }
        )

        ListItem(
            headlineContent = { Text("Due Date") },
            supportingContent = { Text(uiState.value.dueDate.toSimpleDateString()) },
            leadingContent = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
            trailingContent = {
                TextButton(onClick = { showDatePicker = true }) {
                    Text("Change")
                }
            }
        )

        ListItem(
            headlineContent = { Text("Due Time") },
            supportingContent = { Text(uiState.value.dueDate.toSimpleTimeString()) },
            leadingContent = { Icon(Icons.Default.AccessTime, contentDescription = null) },
            trailingContent = {
                TextButton(onClick = { showTimePicker = true }) {
                    Text("Change")
                }
            }
        )

        ListItem(
            headlineContent = { Text("Priority") },
            supportingContent = { Text(uiState.value.priority.name) },
            leadingContent = { Icon(Icons.Default.Flag, contentDescription = null) },
            trailingContent = {
                Box {
                    TextButton(onClick = { showPriorityMenu = true }) {
                        Text("Set")
                    }
                    DropdownMenu(
                        expanded = showPriorityMenu,
                        onDismissRequest = { showPriorityMenu = false }
                    ) {
                        Priority.entries.forEach { priority ->
                            DropdownMenuItem(
                                text = { Text(text = priority.name) },
                                onClick = {
                                    onPrioritySelected(priority)
                                    showPriorityMenu = false
                                }
                            )
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSubmitClicked(onNavigateBack) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Save Task")
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
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                title = { Text("Select Time") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val newTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                            val currentDateTime = uiState.value.dueDate
                            onDateTimeChanged(newTime.atDate(currentDateTime.toLocalDate()))
                            showTimePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                TimePicker(state = timePickerState)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEditScreenComposablePreview() {
    val dummyItem = dummyTasks[0].toAddTaskUiState()
    MaterialTheme {
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
}
