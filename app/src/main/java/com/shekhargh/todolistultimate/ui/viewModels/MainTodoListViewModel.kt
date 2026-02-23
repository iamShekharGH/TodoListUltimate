package com.shekhargh.todolistultimate.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.data.usecase.GetAllTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainTodoListViewModel @Inject constructor(
    getAllTasksUseCase: GetAllTasksUseCase
) : ViewModel() {

    private val _permissionStatus = MutableStateFlow(PermissionStatus.UNKNOWN)
    val permissionStatus = _permissionStatus.asStateFlow()

    val uiState: StateFlow<UiState> = getAllTasksUseCase().map { list ->
        if (list.isEmpty()) UiState.Empty else UiState.ItemsReceived(list)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )

    fun onPermissionResult(isGranted: Boolean) {
        _permissionStatus.value = if (isGranted)
            PermissionStatus.RESOLVED
        else PermissionStatus.UNKNOWN
    }

    /*
    Lifecycle Safe: WhileSubscribed(5000) means if the user rotates the screen, it won't restart the flow. If they leave the app, it stops listening to the database to save battery.
    Automatic: You don't need to remember to call getAllTasks() in init {}.
     */


}

sealed class UiState {
    object Loading : UiState()
    object Empty : UiState()
    data class ItemsReceived(val items: List<TodoTaskItem>) : UiState()
}

enum class PermissionStatus {
    UNKNOWN,
    RESOLVED
}