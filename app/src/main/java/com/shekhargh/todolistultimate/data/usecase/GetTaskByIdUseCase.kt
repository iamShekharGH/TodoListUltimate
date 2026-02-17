package com.shekhargh.todolistultimate.data.usecase

import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.domain.SuspendUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val repository: TodoListUltimateRepository
) : SuspendUseCase<Int, TodoTaskItem?> {
    override suspend fun invoke(input: Int): TodoTaskItem? {
        return repository.getTaskById(input)
    }
}