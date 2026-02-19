package com.shekhargh.todolistultimate.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


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
        return date.toInstant(ZoneOffset.UTC).toEpochMilli().toString()
    }

    @TypeConverter
    fun stringToDate(date: String): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.toLong()), ZoneOffset.UTC)
    }


}