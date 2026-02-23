package com.shekhargh.todolistultimate.usecase

import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.usecase.GetWidgetTaskUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.util.dummyTasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetWidgetTaskUseCaseTest {

    private val repository: TodoListUltimateRepository = mockk(relaxed = true)
    private lateinit var sut: GetWidgetTaskUseCase

    @Before
    fun setup() {
        sut = GetWidgetTaskUseCase(repository)
    }

    @Test
    fun `given repository has tasks , when use case is invoked, then it returns a list of tasks`() =
        runTest {
            coEvery { repository.getWidgetTasks() } returns dummyTasks

            val tasks = sut()

            assertThat(tasks).isEqualTo(dummyTasks)
            coVerify(exactly = 1) { repository.getWidgetTasks() }

        }
}