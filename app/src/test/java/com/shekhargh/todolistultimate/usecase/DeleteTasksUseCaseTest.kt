package com.shekhargh.todolistultimate.usecase

import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.usecase.DeleteTaskUseCase
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.util.dummyTasks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteTasksUseCaseTest {

    private val repository: TodoListUltimateRepository = mockk()
    lateinit var useCase: DeleteTaskUseCase


    @Test
    fun `invoke the delete function from the repository`() = runTest {

        coEvery { repository.deleteItem(any()) } returns 8
        useCase = DeleteTaskUseCase(repository)

        val item = useCase(dummyTasks[5].id)

        assertThat(item).isEqualTo(8)

    }
}