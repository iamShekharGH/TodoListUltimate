package com.shekhargh.todolistultimate.domain

import com.shekhargh.todolistultimate.data.TodoTaskItem
import kotlinx.coroutines.flow.Flow

interface TodoListUltimateRepository {
    fun getAllTasks(): Flow<List<TodoTaskItem>>
    fun getAllDummyTasks(): Flow<List<TodoTaskItem>>

    suspend fun getWidgetTasks(): List<TodoTaskItem>
    suspend fun insertItem(item: TodoTaskItem) : Long
    suspend fun deleteItem(item: Int) : Int
    suspend fun updateItem(item: TodoTaskItem) : Int

    suspend fun updateItemCompletionStatus(isItDone: Boolean,id: Int) : Int
    suspend fun getTaskById(id: Int) : TodoTaskItem?

}