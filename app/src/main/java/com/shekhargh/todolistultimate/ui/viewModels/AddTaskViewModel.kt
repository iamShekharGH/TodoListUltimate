package com.shekhargh.todolistultimate.ui.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shekhargh.todolistultimate.data.Priority
import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.data.usecase.DeleteTaskUseCase
import com.shekhargh.todolistultimate.data.usecase.GetTaskByIdUseCase
import com.shekhargh.todolistultimate.data.usecase.InsertItemUseCase
import com.shekhargh.todolistultimate.data.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val insertItemUseCase: InsertItemUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTaskUiState(title = "", description = ""))
    val uiState = _uiState.asStateFlow()

    private val taskId: Int? = savedStateHandle["task_id"]

    init {
        if (taskId != null && taskId != -1) {
            viewModelScope.launch {
                getTaskByIdUseCase(taskId)?.also { task ->
                    _uiState.update { task.toAddTaskUiState() }
                }
            }
        }
    }

    fun onTitleChanges(title: String) {
        _uiState.update { currentState ->
            currentState.copy(title = title)
        }
    }

    fun onDescriptionChanges(description: String) {
        _uiState.update { currentState ->
            currentState.copy(description = description)
        }
    }

    fun onDoneCheck(isItDone: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isItDone = isItDone)
        }
    }

    fun onPrioritySelected(priority: Priority) {
        _uiState.update { currentState ->
            currentState.copy(priority = priority)
        }
    }

    fun onSubmitClicked(onNavigateBack: () -> Unit) {
        viewModelScope.launch {
            val taskToSave = uiState.value.changeToInputObject()
            var resultId: Number = if (taskId == null || taskId == -1) {
                insertItemUseCase(taskToSave)
            } else {
                updateTaskUseCase(taskToSave.copy(id = taskId))
            }
            if (resultId == 1){
                onNavigateBack()
            }

        }
    }

    fun onDeleteClicked(onNavigateBack: () -> Unit) {
        viewModelScope.launch {
            val deleteConfirmed = deleteTaskUseCase(uiState.value.id)
            if (deleteConfirmed == 1) onNavigateBack()
        }
    }
}

fun TodoTaskItem.toAddTaskUiState(): AddTaskUiState {
    return AddTaskUiState(
        id = id,
        title = title,
        description = description,
        isItDone = isItDone,
        dueDate = dueDate,
        priority = priority,
        tags = tags
    )
}

data class AddTaskUiState(
    val id: Int = 0,
    val title: String,
    val description: String,
    val isItDone: Boolean = false,
    val dueDate: LocalDateTime = LocalDateTime.now(),
    val priority: Priority = Priority.LOW,
    val tags: List<String> = emptyList()
)

fun AddTaskUiState.changeToInputObject(): TodoTaskItem {
    return TodoTaskItem(
        id = id,
        title = title,
        description = description,
        isItDone = isItDone,
        dueDate = dueDate,
        priority = priority,
        tags = tags
    )
}
