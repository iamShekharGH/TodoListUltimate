package com.shekhargh.todolistultimate.ui.composables

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.ui.viewModels.MainTodoListViewModel
import com.shekhargh.todolistultimate.ui.viewModels.PermissionStatus
import com.shekhargh.todolistultimate.ui.viewModels.UiState

@Composable
fun MainScreen(
    viewModel: MainTodoListViewModel = hiltViewModel(),
    onNavigateToTask: (Int) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val permissionStatus = viewModel.permissionStatus.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            viewModel.onPermissionResult(it)
        }
    )

    LaunchedEffect(permissionStatus.value) {
        if (permissionStatus.value == PermissionStatus.UNKNOWN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val isAlreadyGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

                if (isAlreadyGranted) {
                    viewModel.onPermissionResult(true)
                } else {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            viewModel.onPermissionResult(true)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButton(
            onClick = {
                onNavigateToTask(-1)
            }
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new task")

        }
    }) { innerPadding ->
        when (val ui = uiState.value) {
            UiState.Empty -> {
                EmptyScreenComposable(innerPadding)
            }

            is UiState.ItemsReceived -> {
                MainScreenComposable(
                    innerPadding,
                    ui.items,
                    onNavigateToTask,
                    viewModel::onUpdateCompletionStatue
                )
            }

            UiState.Loading -> {
                LoadingScreenComposable(innerPadding)
            }
        }

    }
}

@Composable
fun LoadingScreenComposable(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Loading", fontSize = 21.sp)
        CircularProgressIndicator()
    }
}

@Composable
fun MainScreenComposable(
    paddingValues: PaddingValues,
    items: List<TodoTaskItem>,
    onNavigateToTask: (Int) -> Unit,
    onUpdateCompletionStatue: (Int, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items) { todoTaskItem ->
            TodoItemCard(
                todoTaskItem = todoTaskItem,
                onNavigateToTask = onNavigateToTask,
                onUpdateCompletionStatue = onUpdateCompletionStatue
            )
        }
    }

}

@Composable
fun EmptyScreenComposable(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add your first todo item.", fontSize = 21.sp)
        OutlinedButton(
            onClick = {},
        ) {
            Text(text = "Add Todo.")
        }
    }
}

