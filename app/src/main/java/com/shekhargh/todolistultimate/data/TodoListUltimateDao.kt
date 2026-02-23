package com.shekhargh.todolistultimate.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListUltimateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: TodoTaskItem): Long

    @Query("DELETE FROM TODO_LIST_ULTIMATE_TABLE WHERE id = :itemId")
    suspend fun deleteItem(itemId: Int): Int

    @Update
    suspend fun updateItem(item: TodoTaskItem): Int

    @Query("UPDATE TODO_LIST_ULTIMATE_TABLE SET isItDone = :isItDone WHERE id = :id")
    suspend fun updateItemCompletionStatus(isItDone: Boolean, id: Int): Int


    @Query("SELECT * FROM TODO_LIST_ULTIMATE_TABLE ORDER BY dueDate ASC")
    fun getAllItems(): Flow<List<TodoTaskItem>>

    @Query("SELECT * FROM TODO_LIST_ULTIMATE_TABLE")
    suspend fun getWidgetTasks(): List<TodoTaskItem>

    @Query("SELECT * FROM TODO_LIST_ULTIMATE_TABLE WHERE id = :id")
    suspend fun getItemById(id: Int): TodoTaskItem?
}