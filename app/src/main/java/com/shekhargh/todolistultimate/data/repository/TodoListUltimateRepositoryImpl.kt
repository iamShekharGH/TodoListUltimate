package com.shekhargh.todolistultimate.data.repository

import com.shekhargh.todolistultimate.data.TodoListUltimateDao
import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.util.dummyTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


class TodoListUltimateRepositoryImpl @Inject constructor(
    private val dao: TodoListUltimateDao,
) : TodoListUltimateRepository {

    override fun getAllTasks(): Flow<List<TodoTaskItem>> = dao.getAllItems()

    override fun getAllDummyTasks(): Flow<List<TodoTaskItem>> = flowOf(dummyTasks)

    override suspend fun insertItem(item: TodoTaskItem) = dao.insertItem(item)

    override suspend fun deleteItem(item: TodoTaskItem) = dao.deleteItem(item)

    override suspend fun updateItem(item: TodoTaskItem) = dao.updateItem(item)

    override suspend fun updateItemCompletionStatus(isItDone: Boolean, id: Int) = dao.updateItemCompletionStatus(isItDone,id)

    override suspend fun getTaskById(id: Int): TodoTaskItem? = dao.getItemById(id)

}