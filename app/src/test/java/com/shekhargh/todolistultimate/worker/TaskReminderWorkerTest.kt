package com.shekhargh.todolistultimate.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.shekhargh.todolistultimate.data.usecase.GetAllTasksUseCase
import com.shekhargh.todolistultimate.data.usecase.GetTaskByIdUseCase
import com.shekhargh.todolistultimate.util.dummyTasks
import com.shekhargh.todolistultimate.worker.notification.TaskNotifier
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TaskReminderWorkerTest {

    private val getAllTasksUseCase: GetAllTasksUseCase = mockk(relaxed = true)
    private val getTaskByIdUseCase: GetTaskByIdUseCase = mockk(relaxed = true)
    private val taskNotifier: TaskNotifier = mockk(relaxed = true)

    private lateinit var factory: WorkerFactory

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        factory = object : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker {
                return TaskReminderWorker(
                    appContext,
                    workerParameters,
//                    getAllTasksUseCase,
                    getTaskByIdUseCase,
                    taskNotifier
                )
            }
        }
    }

    @Test
    fun givenValidTaskId_whenDoWork_thenNotificationIsShown() = runTest {
        val task = dummyTasks[0].copy(id = 1, isItDone = false)
        coEvery { getTaskByIdUseCase(1) } returns task

        val inputData = workDataOf("task_id" to 1)
        val worker = TestListenableWorkerBuilder<TaskReminderWorker>(context)
            .setWorkerFactory(factory)
            .setInputData(inputData)
            .build()

        val result = worker.doWork()

        assert(result == ListenableWorker.Result.success())
        coVerify(exactly = 1) { taskNotifier.showNotification(task) }

    }


}