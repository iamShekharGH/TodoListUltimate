package com.shekhargh.todolistultimate.data.usecase

import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.domain.NoParameterUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: TodoListUltimateRepository
) : NoParameterUseCase<Flow<List<TodoTaskItem>>> {
    override fun invoke(): Flow<List<TodoTaskItem>> {
        return repository.getAllTasks()
    }
}