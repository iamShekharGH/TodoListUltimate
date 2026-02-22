package com.shekhargh.todolistultimate.domain

import com.shekhargh.todolistultimate.data.TodoTaskItem

interface TaskSchedular {
    fun scheduleTask(task: TodoTaskItem)
    fun cancelTask(id: Int)
}