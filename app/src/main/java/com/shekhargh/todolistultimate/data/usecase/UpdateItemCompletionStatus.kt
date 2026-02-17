package com.shekhargh.todolistultimate.data.usecase

import com.shekhargh.todolistultimate.domain.SuspendUseCaseMultipleInput
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import javax.inject.Inject

class UpdateItemCompletionStatus @Inject constructor(
    private val repository: TodoListUltimateRepository
) : SuspendUseCaseMultipleInput<Boolean, Int, Int> {
    override suspend fun invoke(input: Boolean, input2: Int): Int {
        return repository.updateItemCompletionStatus(input, input2)
    }
}