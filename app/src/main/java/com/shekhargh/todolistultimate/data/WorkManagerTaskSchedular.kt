package com.shekhargh.todolistultimate.data

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.shekhargh.todolistultimate.domain.TaskSchedular
import com.shekhargh.todolistultimate.worker.TaskReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerTaskSchedular @Inject constructor(
    @param:ApplicationContext private val context: Context
) : TaskSchedular {

    override fun scheduleTask(task: TodoTaskItem) {
        val delay = Duration.between(LocalDateTime.now(), task.dueDate).toMillis()
        if (delay <= 0) return

        val request = OneTimeWorkRequestBuilder<TaskReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("task_id" to task.id))
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "task_${task.id}",
            ExistingWorkPolicy.REPLACE,
            request
        )

    }

    override fun cancelTask(id: Int) {
        WorkManager.getInstance(context).cancelUniqueWork("task_$id")
    }
}