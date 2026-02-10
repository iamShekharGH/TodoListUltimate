package com.shekhargh.todolistultimate

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.TodoListUltimateDao
import com.shekhargh.todolistultimate.data.TodoListUltimateDatabase
import com.shekhargh.todolistultimate.util.dummyTasks
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoListUltimateDaoTest {

    private lateinit var dao: TodoListUltimateDao
    private lateinit var db: TodoListUltimateDatabase


    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoListUltimateDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.getDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertedItemInDB() = runTest {
        val item = dummyTasks[0]
        val id = dao.insertItem(item)
        assertThat(id).isGreaterThan(0)

        val items = dao.getAllItems().first()

        assertThat(item).isEqualTo(items.first().copy(id = item.id))

    }

    @Test
    fun updateItem_changeData() = runTest {
        val item = dummyTasks[1]
        dao.insertItem(item)
        var items = dao.getAllItems().first()
        assertThat(item).isEqualTo(items.first().copy(id = item.id))

        var updateThisItem = items.first()
        updateThisItem = updateThisItem.copy(title = "This has changed!!!")

        dao.updateItem(updateThisItem)
        items = dao.getAllItems().first()
        assertThat(items).contains(updateThisItem)

    }

    @Test
    fun deleteTask() = runTest {
        val item = dummyTasks[2]
        dao.insertItem(item)
        val items = dao.getAllItems().first()
        assertThat(item).isEqualTo(items.first().copy(id = item.id))
        dao.deleteItem(items.first())

        assertThat(dao.getAllItems().first()).isEmpty()

    }

    @Test
    fun updateItemDoneStatus() = runTest {
        val item = dummyTasks[3]
        dao.insertItem(item)
        val insertedItem = dao.getAllItems().first().first()
        assertThat(item).isEqualTo(insertedItem.copy(id = item.id))
        dao.updateItemCompletionStatus(true, item.id)
        val updatedItem = dao.getItemById(insertedItem.id)
        assertThat(updatedItem?.isItDone).isEqualTo(true)
    }

    @Test
    fun getTheItemById() = runTest {
        val item = dummyTasks[4]
        dao.insertItem(item)
        var insertedItem = dao.getAllItems().first().first()
        val getItem = dao.getItemById(insertedItem.id)
        assertThat(getItem).isEqualTo(insertedItem)

    }


}