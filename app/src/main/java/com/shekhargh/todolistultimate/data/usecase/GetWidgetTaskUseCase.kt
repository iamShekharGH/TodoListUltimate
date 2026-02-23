package com.shekhargh.todolistultimate.data.usecase

import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.domain.NoParameterSuspendUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import javax.inject.Inject

class GetWidgetTaskUseCase @Inject constructor(
    private val repository: TodoListUltimateRepository
) : NoParameterSuspendUseCase<List<TodoTaskItem>> {

    override suspend fun invoke(): List<TodoTaskItem> {
        return repository.getWidgetTasks()
    }
}