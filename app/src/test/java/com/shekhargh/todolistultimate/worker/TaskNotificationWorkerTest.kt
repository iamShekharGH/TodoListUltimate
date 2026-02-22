package com.shekhargh.todolistultimate.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.ListenableWorker.Result
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.usecase.GetAllTasksUseCase
import com.shekhargh.todolistultimate.data.usecase.GetTaskByIdUseCase
import com.shekhargh.todolistultimate.util.dummyTasks
import com.shekhargh.todolistultimate.worker.notification.TaskNotifier
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime


@RunWith(RobolectricTestRunner::class)
class TaskNotificationWorkerTest {

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
                return TaskNotificationWorker(
                    appContext,
                    workerParameters,
                    getAllTasksUseCase,
                    getTaskByIdUseCase,
                    taskNotifier
                )
            }
        }
    }


    @Test
    fun test_doWork_ReturnSuccess() = runTest {
        val worker = TestListenableWorkerBuilder<TaskNotificationWorker>(context)
            .setWorkerFactory(factory)
            .build()

        val result = worker.doWork()

        assertThat(result).isEqualTo(Result.success())

    }

    @Test
    fun givenTaskExist_whenDoWork_thenGetAllTasksUseCaseIsCalled() = runTest {
        coEvery { getAllTasksUseCase() } returns flowOf(dummyTasks)

        val worker =
            TestListenableWorkerBuilder<TaskNotificationWorker>(context).setWorkerFactory(factory)
                .build()
        worker.doWork()
        coVerify(exactly = 1) { getAllTasksUseCase() }

    }

    @Test
    fun givenTaskIsDue_whenDoWork_thenNotifierIsCalled() = runTest {
        val localDateTime = LocalDateTime.now()
        val dueTask = dummyTasks[0].copy(dueDate = localDateTime, isItDone = false)
        coEvery { getAllTasksUseCase() } returns flowOf(listOf(dueTask))

        val worker =
            TestListenableWorkerBuilder<TaskNotificationWorker>(context).setWorkerFactory(factory)
                .build()
        worker.doWork()
        coVerify(exactly = 1) { taskNotifier.showNotification(dueTask) }
    }

    @Test
    fun givenTaskIsNotDue_whenDoWork_thenNotifierIsNotCalled() = runTest {
        val localDateTime = LocalDateTime.now()
        val dueTask = dummyTasks[0].copy(dueDate = localDateTime, isItDone = true)
        coEvery { getAllTasksUseCase() } returns flowOf(listOf(dueTask))

        val worker =
            TestListenableWorkerBuilder<TaskNotificationWorker>(context).setWorkerFactory(factory)
                .build()
        worker.doWork()
        coVerify(exactly = 0) { taskNotifier.showNotification(dueTask) }
    }

}