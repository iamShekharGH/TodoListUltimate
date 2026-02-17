package com.shekhargh.todolistultimate.usecase

import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.usecase.InsertItemUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.util.dummyTasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class InsertItemUseCaseTest {

    private val repository: TodoListUltimateRepository = mockk()
    private lateinit var sut: InsertItemUseCase

    @Before
    fun setup() {
        sut = InsertItemUseCase(repository)
    }

    @Test
    fun `given task item, when use case is invoked, then inserts item and returns its new id`() =
        runTest {

            val itemToInsert = dummyTasks[5]
            val expectedId = 1L
            coEvery { repository.insertItem(itemToInsert) } returns expectedId

            val actualId = sut(itemToInsert)
            assertThat(actualId).isEqualTo(expectedId)

            coVerify(exactly = 1) { repository.insertItem(itemToInsert) }

        }
}