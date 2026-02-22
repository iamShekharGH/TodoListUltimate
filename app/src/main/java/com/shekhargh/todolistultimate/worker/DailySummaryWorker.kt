package com.shekhargh.todolistultimate.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shekhargh.todolistultimate.data.usecase.GetAllTasksUseCase
import com.shekhargh.todolistultimate.data.usecase.GetTaskByIdUseCase
import com.shekhargh.todolistultimate.worker.notification.TaskNotifier
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@HiltWorker
class DailySummaryWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val taskNotifier: TaskNotifier
) : CoroutineWorker(appContext, params) {


    override suspend fun doWork(): Result {
        val tasks = getAllTasksUseCase().first()
        val today = LocalDate.now()

        val filteredTasks = tasks.filter { !it.isItDone && it.dueDate.toLocalDate() == today }
        println(filteredTasks.toString())
        filteredTasks.forEach { taskNotifier.showNotification(it) }
        return Result.success()
    }
}