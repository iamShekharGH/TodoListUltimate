package com.shekhargh.todolistultimate.data.usecase

import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.domain.SuspendUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TodoListUltimateRepository
) : SuspendUseCase<TodoTaskItem, Int> {
    override suspend fun invoke(input: TodoTaskItem): Int {
        return repository.updateItem(input)
    }
}