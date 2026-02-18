package com.shekhargh.todolistultimate.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.data.usecase.GetAllTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainTodoListViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = getAllTasksUseCase().map { list ->
        if (list.isEmpty()) UiState.Empty else UiState.ItemsReceived(list)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )
    /*
    Lifecycle Safe: WhileSubscribed(5000) means if the user rotates the screen, it won't restart the flow. If they leave the app, it stops listening to the database to save battery.
    Automatic: You don't need to remember to call getAllTasks() in init {}.
     */


    fun getAllTasks() {
        viewModelScope.launch {
            getAllTasksUseCase().collect { _uiState.value = it as UiState }
        }
    }


}

sealed class UiState {
    object Loading : UiState()
    object Empty : UiState()
    data class ItemsReceived(val items: List<TodoTaskItem>) : UiState()
}