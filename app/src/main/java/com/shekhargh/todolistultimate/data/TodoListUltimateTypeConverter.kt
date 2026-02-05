package com.shekhargh.todolistultimate.data

import androidx.room.TypeConverter
import java.time.LocalDateTime


class TodoListUltimateTypeConverter {

    @TypeConverter
    fun priorityToString(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun stringToPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun stringToList(text: String): List<String> {
        return text.split(",")
    }

    @TypeConverter
    fun dateToString(date: LocalDateTime): String {
        return date.toString()
    }

    @TypeConverter
    fun stringToDate(date: String): LocalDateTime {
        return LocalDateTime.parse(date)
    }


}