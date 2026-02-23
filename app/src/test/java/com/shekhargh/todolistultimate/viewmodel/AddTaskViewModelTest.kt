package com.shekhargh.todolistultimate.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.shekhargh.todolistultimate.data.usecase.DeleteTaskUseCase
import com.shekhargh.todolistultimate.data.usecase.GetTaskByIdUseCase
import com.shekhargh.todolistultimate.data.usecase.InsertItemUseCase
import com.shekhargh.todolistultimate.data.usecase.UpdateTaskUseCase
import com.shekhargh.todolistultimate.domain.TaskSchedular
import com.shekhargh.todolistultimate.domain.WidgetUpdater
import com.shekhargh.todolistultimate.ui.viewModels.AddTaskViewModel
import com.shekhargh.todolistultimate.ui.viewModels.changeToInputObject
import com.shekhargh.todolistultimate.util.MainDispatcherRule
import com.shekhargh.todolistultimate.util.dummyTasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class AddTaskViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val insertItemUseCase: InsertItemUseCase = mockk(relaxed = true)
    private val updateTaskUseCase: UpdateTaskUseCase = mockk(relaxed = true)

    private val deleteTaskUseCase: DeleteTaskUseCase = mockk(relaxed = true)
    private val getTaskByIdUseCase: GetTaskByIdUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()

    private val widgetUpdater: WidgetUpdater = mockk(relaxed = true)

    private val taskSchedular: TaskSchedular = mockk(relaxed = true)

    private lateinit var sut: AddTaskViewModel


    @Test
    fun `given new task data, when onSubmitClicked is called, then insert use case is invoked`() =
        runTest {
            coEvery { savedStateHandle.get<Int>("task_id") } returns null

            sut = AddTaskViewModel(
                insertItemUseCase,
                updateTaskUseCase,
                getTaskByIdUseCase,
                deleteTaskUseCase,
                taskSchedular,
                widgetUpdater,
                savedStateHandle
            )
            val title = "New Test Task"
            val description = "A description for the test task"
            sut.onTitleChanges(title)
            sut.onDescriptionChanges(description)

            val taskToInsert = sut.uiState.value.changeToInputObject()

            sut.onSubmitClicked(onNavigateBack = {})
            coVerify(exactly = 1) { insertItemUseCase(taskToInsert) }
            coVerify(exactly = 0) { updateTaskUseCase(any()) }

        }

    @Test
    fun `given existing task data, when onSubmitClicked is called, then update use case is invoked`() =
        runTest {
            val existingTask = dummyTasks[0]
            coEvery { savedStateHandle.get<Int>("task_id") } returns existingTask.id
            coEvery { getTaskByIdUseCase(existingTask.id) } returns existingTask

            sut = AddTaskViewModel(
                insertItemUseCase,
                updateTaskUseCase,
                getTaskByIdUseCase,
                deleteTaskUseCase,
                taskSchedular,
                widgetUpdater,
                savedStateHandle
            )

            val updatedTitle = "Updated Title"
            sut.onTitleChanges(updatedTitle)

            val taskToUpdate = sut.uiState.value.changeToInputObject()

            sut.onSubmitClicked(onNavigateBack = {})

            coVerify(exactly = 0) { insertItemUseCase(any()) }
            coVerify(exactly = 1) { updateTaskUseCase(taskToUpdate) }
        }

    @Test
    fun `given a task id, when viewmodel is created, then ui state is updated with task data`() =
        runTest {
            val existingTask = dummyTasks[1]
            coEvery { savedStateHandle.get<Int>("task_id") } returns existingTask.id
            coEvery { getTaskByIdUseCase(existingTask.id) } returns existingTask

            sut = AddTaskViewModel(
                insertItemUseCase,
                updateTaskUseCase,
                getTaskByIdUseCase,
                deleteTaskUseCase,
                taskSchedular,
                widgetUpdater,
                savedStateHandle
            )

            assert(sut.uiState.value.title == existingTask.title)
            assert(sut.uiState.value.description == existingTask.description)
        }

    @Test
    fun `given task is loaded, when onDeleteClicked is called, then delete use case is invoked`() =
        runTest {
            val taskItem = dummyTasks[7]
            val taskToDeleteId = taskItem.id
            coEvery { deleteTaskUseCase(taskToDeleteId) } returns 1
            coEvery { savedStateHandle.get<Int>("task_id") } returns taskToDeleteId
            coEvery { getTaskByIdUseCase(taskToDeleteId) } returns taskItem

            sut = AddTaskViewModel(
                insertItemUseCase,
                updateTaskUseCase,
                getTaskByIdUseCase,
                deleteTaskUseCase,
                taskSchedular,
                widgetUpdater,
                savedStateHandle
            )

            sut.onDeleteClicked({})

            coVerify(exactly = 1) { deleteTaskUseCase(taskToDeleteId) }


        }

    @Test
    fun `give valid task when submit it clicked then task is scheduled`() =
        runTest {
            val futureDate = LocalDateTime.now().plusHours(1)
            val task = dummyTasks[9].copy(id = 1, dueDate = futureDate)

            coEvery { insertItemUseCase(any()) } returns 1
            coEvery { updateTaskUseCase(any()) } returns 1
            coEvery { savedStateHandle.get<Int>("task_id") } returns task.id
            coEvery { getTaskByIdUseCase(task.id) } returns task

            sut = AddTaskViewModel(
                insertItemUseCase,
                updateTaskUseCase,
                getTaskByIdUseCase,
                deleteTaskUseCase,
                taskSchedular,
                widgetUpdater,
                savedStateHandle
            )

            sut.onSubmitClicked { }

            verify(exactly = 1) {
                taskSchedular.scheduleTask(any())
            }
        }


    @Test
    fun `given existing task when delete it clicked then task is cancelled`() =
        runTest {
            val futureDate = LocalDateTime.now().plusHours(1)
            val task = dummyTasks[9].copy(id = 1, dueDate = futureDate)

            coEvery { deleteTaskUseCase(any()) } returns 1
            coEvery { savedStateHandle.get<Int>("task_id") } returns task.id
            coEvery { getTaskByIdUseCase(task.id) } returns task

            sut = AddTaskViewModel(
                insertItemUseCase,
                updateTaskUseCase,
                getTaskByIdUseCase,
                deleteTaskUseCase,
                taskSchedular,
                widgetUpdater,
                savedStateHandle
            )

            sut.onDeleteClicked { }

            verify(exactly = 1) {
                taskSchedular.cancelTask(any())
            }
        }

    @Test
    fun `given item is updated when update is clicked then widget is updated`() = runTest {

        val taskItem = dummyTasks[7]
        val taskToDeleteId = taskItem.id
        coEvery { deleteTaskUseCase(taskToDeleteId) } returns 1
        coEvery { savedStateHandle.get<Int>("task_id") } returns taskToDeleteId
        coEvery { getTaskByIdUseCase(taskToDeleteId) } returns taskItem
        coEvery { insertItemUseCase(taskItem) } returns 1
        coEvery { updateTaskUseCase(taskItem) } returns 1

        sut = AddTaskViewModel(
            insertItemUseCase,
            updateTaskUseCase,
            getTaskByIdUseCase,
            deleteTaskUseCase,
            taskSchedular,
            widgetUpdater,
            savedStateHandle
        )

        sut.onSubmitClicked { }

        coVerify(exactly = 1) { widgetUpdater.updateWidget() }

    }
}
