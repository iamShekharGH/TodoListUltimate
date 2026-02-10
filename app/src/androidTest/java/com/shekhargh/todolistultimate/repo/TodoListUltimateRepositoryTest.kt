package com.shekhargh.todolistultimate.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.TodoListUltimateDao
import com.shekhargh.todolistultimate.data.repository.TodoListUltimateRepositoryImpl
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.util.dummyTasks
import io.mockk.coEvery
import io.mockk.coVerify
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

    @Test
    fun insertItemToDb() = runTest {
        val item = dummyTasks[2]
        coEvery { dao.insertItem(item) } returns 3L
        coEvery { dao.getItemById(item.id) } returns item
        repository.insertItem(item)
        coVerify(exactly = 1) { dao.insertItem(item) }
        val newItem = repository.getTaskById(item.id)

        assertThat(newItem).isEqualTo(item)

    }

    @Test
    fun deleteItemFromDb() = runTest {
        val item = dummyTasks[0]
        coEvery { dao.deleteItem(item) } returns 0
        val done = repository.deleteItem(item)
        assertThat(done).isEqualTo(0)

    }

    @Test
    fun updateItemFromDb() = runTest {
        val item = dummyTasks[0]
        coEvery { dao.updateItem(item) } returns item.id
        val done = repository.updateItem(item)
        assertThat(done).isEqualTo(item.id)
    }

    @Test
    fun updateItemCompletionStatus() = runTest {
        val item = dummyTasks[3]
        coEvery { dao.updateItemCompletionStatus(isItDone = true, id = item.id) } returns item.id
        coEvery { dao.getItemById(item.id) } returns item.copy(isItDone = true)
        repository.updateItemCompletionStatus(true, item.id)
        val updatedItem = repository.getTaskById(item.id)
        assertThat(updatedItem.isItDone).isEqualTo(true)
    }

    @Test
    fun getDummyDataInstead() = runTest {
        val items = repository.getAllDummyTasks().first()
        assertThat(items).isEqualTo(dummyTasks)
    }

}