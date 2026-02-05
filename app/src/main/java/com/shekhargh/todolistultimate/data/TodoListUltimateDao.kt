package com.shekhargh.todolistultimate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListUltimateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: TodoTaskItem): Int

    @Delete
    suspend fun deleteItem(item: TodoTaskItem): Int

    @Update
    suspend fun updateItem(item: TodoTaskItem): Int

    @Query("SELECT * FROM TODO_LIST_ULTIMATE_TABLE")
    fun getAllItems(): Flow<List<TodoTaskItem>>
}