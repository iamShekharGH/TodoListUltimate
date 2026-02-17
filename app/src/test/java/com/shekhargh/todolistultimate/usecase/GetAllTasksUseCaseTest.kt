package com.shekhargh.todolistultimate.usecase

import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.usecase.GetAllTasksUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.util.dummyTasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetAllTasksUseCaseTest {

    private val repository: TodoListUltimateRepository = mockk()
    private lateinit var useCase: GetAllTasksUseCase


    @Test
    fun `invoke calls repository and returns flow of tasks`() = runTest {

        every { repository.getAllTasks() } returns flowOf(dummyTasks)

        useCase = GetAllTasksUseCase(repository)

        val result = useCase().first()

        assertThat(result).isEqualTo(dummyTasks)
        verify(exactly = 1) { repository.getAllTasks() }


    }
}