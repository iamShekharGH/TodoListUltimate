package com.shekhargh.todolistultimate.usecase

import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.usecase.GetTaskByIdUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.util.dummyTasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetTaskByIdUseCaseTest {

    private val repository: TodoListUltimateRepository = mockk()
    private lateinit var sut: GetTaskByIdUseCase

    @Before
    fun setup() {
        sut = GetTaskByIdUseCase(repository)
    }

    @Test
    fun `given a task id ,when use case is invoked ,then it returns a Task object`() = runTest {
        val expectedItem = dummyTasks[7]
        val validId = expectedItem.id
        coEvery { repository.getTaskById(validId) } returns expectedItem

        val actualItem = sut(validId)

        assertThat(actualItem).isEqualTo(expectedItem)

        coVerify(exactly = 1) { repository.getTaskById(any()) }

    }

    @Test
    fun `given a task id ,when use case is invoked ,then it returns null object`() = runTest {
//        val itemForReturn = dummyTasks[7]
        val nonExistentId = dummyTasks[7].id
        coEvery { repository.getTaskById(nonExistentId) } returns null

        val returnedItem = sut(nonExistentId)

        assertThat(returnedItem).isNull()

        coVerify(exactly = 1) { repository.getTaskById(nonExistentId) }

    }
}