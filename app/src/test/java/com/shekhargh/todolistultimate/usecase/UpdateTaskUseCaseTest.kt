package com.shekhargh.todolistultimate.usecase

import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.usecase.UpdateTaskUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.util.dummyTasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateTaskUseCaseTest {

    private val repo: TodoListUltimateRepository = mockk()
    private lateinit var sut: UpdateTaskUseCase

    @Before
    fun setup() {
        sut = UpdateTaskUseCase(repo)
    }

    @Test
    fun `given updated task item, when use case is invoked, then updates item and returns success`() =
        runTest {

            val itemToUpdate = dummyTasks[6]
            val expectedRowsAffected = 5
            coEvery { repo.updateItem(itemToUpdate) } returns expectedRowsAffected

            val actualRowsAffected = sut(itemToUpdate)

            assertThat(actualRowsAffected).isEqualTo(expectedRowsAffected)
            coVerify(exactly = 1) { repo.updateItem(itemToUpdate) }

        }
}