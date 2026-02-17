package com.shekhargh.todolistultimate.usecase

import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.usecase.GetAllTasksUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.util.dummyTasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetAllTasksUseCaseTest {

    private val repository: TodoListUltimateRepository = mockk()
    private lateinit var useCase: GetAllTasksUseCase

    @Before
    fun setup() {
        useCase = GetAllTasksUseCase(repository)
    }

    @Test
    fun `given repository has tasks, when use case is invoked, then it returns flow of tasks`() =
        runTest {

            coEvery { repository.getAllTasks() } returns flowOf(dummyTasks)

            val result = useCase().first()

            assertThat(result).isEqualTo(dummyTasks)
            coVerify(exactly = 1) { repository.getAllTasks() }


        }
}