package com.shekhargh.todolistultimate.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.TodoListUltimateDao
import com.shekhargh.todolistultimate.data.repository.TodoListUltimateRepositoryImpl
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.dummyTasks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoListUltimateRepositoryTest {

    private lateinit var repository: TodoListUltimateRepository
    private val dao: TodoListUltimateDao = mockk<TodoListUltimateDao>(relaxed = true)

    @Before
    fun setup() {
        repository = TodoListUltimateRepositoryImpl(dao)
        every { dao.getAllItems() } returns flowOf(dummyTasks)

    }

    @Test
    fun returnsAListOfTaskItems() = runTest {
        val tasks = repository.getAllTasks().first()
        assertThat(tasks).isEqualTo(dummyTasks)
    }

}