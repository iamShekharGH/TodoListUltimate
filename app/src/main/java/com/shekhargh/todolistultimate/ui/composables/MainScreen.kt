package com.shekhargh.todolistultimate.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shekhargh.todolistultimate.ui.viewModels.MainTodoListViewModel
import com.shekhargh.todolistultimate.ui.viewModels.UiState

@Composable
fun MainScreenComposable(
    modifier: Modifier = Modifier,
    viewModel: MainTodoListViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    MainScreenContent(uiState.value)

}


@Composable
fun MainScreenContent(uiState: UiState) {
    Text(text = "State: $uiState")
}