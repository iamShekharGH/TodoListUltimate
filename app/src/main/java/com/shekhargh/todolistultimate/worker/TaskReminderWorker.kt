package com.shekhargh.todolistultimate.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shekhargh.todolistultimate.data.usecase.GetTaskByIdUseCase
import com.shekhargh.todolistultimate.worker.notification.TaskNotifier
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TaskReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val taskNotifier: TaskNotifier
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val taskId = inputData.getInt("task_id", -1)
        if (taskId == -1) return Result.failure()

        val task = getTaskByIdUseCase(taskId)

        if (task != null && !task.isItDone) {
            taskNotifier.showNotification(task)
        }
        return Result.success()
    }
}