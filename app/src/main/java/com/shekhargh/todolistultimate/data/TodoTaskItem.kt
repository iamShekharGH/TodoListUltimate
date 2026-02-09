package com.shekhargh.todolistultimate.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

const val TABLE_NAME = "todo_list_ultimate_table"
const val TASK_MASTER_DATABASE_NAME = "todo_list_ultimate_database"

@Entity(tableName = TABLE_NAME)
data class TodoTaskItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val isItDone: Boolean = false,
    val dueDate: LocalDateTime = LocalDateTime.now(),
    val priority: Priority = Priority.LOW,
    val tags: List<String> = emptyList()
)


enum class Priority {
    LOW, MEDIUM, HIGH
}