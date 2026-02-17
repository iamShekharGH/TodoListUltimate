package com.shekhargh.todolistultimate.data.usecase

import com.shekhargh.todolistultimate.domain.SuspendUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TodoListUltimateRepository
) : SuspendUseCase<Int, Int> {
    override suspend fun invoke(input: Int): Int {
        return repository.deleteItem(input)
    }
}