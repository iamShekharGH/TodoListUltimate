package com.shekhargh.todolistultimate.worker.notification

import com.shekhargh.todolistultimate.data.TodoTaskItem

interface TaskNotifier {
    fun showNotification(task: TodoTaskItem)
}