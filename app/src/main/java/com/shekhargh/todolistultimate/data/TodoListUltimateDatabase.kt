package com.shekhargh.todolistultimate.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TodoTaskItem::class], version = 1)
@TypeConverters(TodoListUltimateTypeConverter::class)
abstract class TodoListUltimateDatabase : RoomDatabase() {
    abstract fun getDao(): TodoListUltimateDao
}