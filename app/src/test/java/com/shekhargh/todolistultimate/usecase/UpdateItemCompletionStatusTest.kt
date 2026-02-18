package com.shekhargh.todolistultimate.usecase

import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.usecase.UpdateCompleteStatusParams
import com.shekhargh.todolistultimate.data.usecase.UpdateItemCompletionStatusUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.util.dummyTasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateItemCompletionStatusTest {

    private val repository: TodoListUltimateRepository = mockk()
    lateinit var sut: UpdateItemCompletionStatusUseCase

    @Before
    fun setup() {
        sut = UpdateItemCompletionStatusUseCase(repository)
    }

    @Test
    fun `given id and boolean, when use cse is invoked, then updates state of todo returns int`() =
        runTest {
            val updateThisItem = dummyTasks[9]
            val updateItemId = updateThisItem.id
            val updateItemState = !updateThisItem.isItDone

            coEvery {
                repository.updateItemCompletionStatus(
                    updateItemState,
                    updateItemId
                )
            } returns updateItemId

            val affectedRows = sut(
                UpdateCompleteStatusParams(updateItemState, updateItemId)
            )

            assertThat(affectedRows).isEqualTo(updateItemId)
            coVerify(exactly = 1) {
                repository.updateItemCompletionStatus(
                    updateItemState,
                    updateItemId
                )
            }


        }
}