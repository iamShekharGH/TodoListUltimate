package com.shekhargh.todolistultimate.data.usecase

import com.shekhargh.todolistultimate.data.TodoTaskItem
import com.shekhargh.todolistultimate.domain.SuspendUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import javax.inject.Inject

class InsertItemUseCase @Inject constructor(
    private val repository: TodoListUltimateRepository
) : SuspendUseCase<TodoTaskItem, Long> {

    override suspend operator fun invoke(input: TodoTaskItem): Long {
        return repository.insertItem(input)
    }
}