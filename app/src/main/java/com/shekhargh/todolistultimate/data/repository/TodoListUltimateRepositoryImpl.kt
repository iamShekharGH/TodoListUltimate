package com.shekhargh.todolistultimate.data.repository

import com.shekhargh.todolistultimate.data.TodoListUltimateDao
import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class TodoListUltimateRepositoryImpl @Inject constructor(
    private val dao: TodoListUltimateDao
) : TodoListUltimateRepository {

    override fun getAllTasks(): Flow<List<TodoTaskItem>> = dao.getAllItems()


}