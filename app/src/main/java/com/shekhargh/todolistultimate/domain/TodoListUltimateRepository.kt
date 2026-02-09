package com.shekhargh.todolistultimate.domain

import com.shekhargh.todolistultimate.data.TodoTaskItem
import kotlinx.coroutines.flow.Flow

interface TodoListUltimateRepository {
    fun getAllTasks(): Flow<List<TodoTaskItem>>
}