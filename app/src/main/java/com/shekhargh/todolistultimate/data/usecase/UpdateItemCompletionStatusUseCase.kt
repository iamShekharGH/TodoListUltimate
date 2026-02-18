package com.shekhargh.todolistultimate.data.usecase

import com.shekhargh.todolistultimate.domain.SuspendUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import javax.inject.Inject

class UpdateItemCompletionStatusUseCase @Inject constructor(
    private val repository: TodoListUltimateRepository
) : SuspendUseCase<UpdateCompleteStatusParams, Int> {
    override suspend fun invoke(input: UpdateCompleteStatusParams): Int {
        return repository.updateItemCompletionStatus(input.isItDone, input.itemId)
    }
}

data class UpdateCompleteStatusParams(
    val isItDone: Boolean,
    val itemId: Int
)