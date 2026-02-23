package com.shekhargh.todolistultimate.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.shekhargh.todolistultimate.data.usecase.GetAllTasksUseCase
import com.shekhargh.todolistultimate.ui.viewModels.MainTodoListViewModel
import com.shekhargh.todolistultimate.ui.viewModels.PermissionStatus
import com.shekhargh.todolistultimate.ui.viewModels.UiState
import com.shekhargh.todolistultimate.util.MainDispatcherRule
import com.shekhargh.todolistultimate.util.dummyTasks
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

class MainTodoListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAllTasksUseCase: GetAllTasksUseCase = mockk(relaxed = true)

    private lateinit var sut: MainTodoListViewModel

    @After
    fun tearDown() {
        clearMocks(getAllTasksUseCase)
    }

    @Test
    fun `given tasks are available, when getAllTasks is called, then uiState emits Loading then ItemsReceived`() =
        runTest {
            coEvery { getAllTasksUseCase() } returns flowOf(dummyTasks)
            sut = MainTodoListViewModel(getAllTasksUseCase)

            /*
                why using job = launch
                UnconfinedTestDispatcher: We explicitly tell our MainDispatcherRule to use an UnconfinedTestDispatcher. This dispatcher starts coroutines eagerly, which helps eliminate the race condition by ensuring the ViewModel's init block and the test's collection start in a more predictable order.
             */

            val job = launch {
                sut.uiState.test {

                    assertThat(awaitItem()).isEqualTo(UiState.Loading)

                    val successItem = awaitItem()
                    assertThat(successItem).isInstanceOf(UiState.ItemsReceived::class.java)
                    assertThat((successItem as UiState.ItemsReceived).items).isEqualTo(dummyTasks)

                    expectNoEvents()
                }
            }

            @Suppress("UnusedFlow")
            coVerify(exactly = 1) { getAllTasksUseCase() }
            job.cancel()

        }

    @Test
    fun `given repository is empty, when ViewModel is initialized, then uiState emits Loading then Empty`() =
        runTest {
            coEvery { getAllTasksUseCase() } returns flowOf(emptyList())
            sut = MainTodoListViewModel(getAllTasksUseCase)

            val job = launch {
                sut.uiState.test {
                    skipItems(1)
                    assertThat(awaitItem()).isEqualTo(UiState.Loading)
                    assertThat(awaitItem()).isEqualTo(UiState.Empty)
                    expectNoEvents()
                }
            }

            @Suppress("UnusedFlow")
            coVerify(exactly = 1) { getAllTasksUseCase() }
            job.cancel()
        }

    @Test
    fun `given initial init, then permission status is UNKNOWN`() = runTest {
        sut = MainTodoListViewModel(getAllTasksUseCase)
        assertThat(sut.permissionStatus.value).isEqualTo(PermissionStatus.UNKNOWN)
    }

    @Test
    fun `when onPermission is called, then permission status updates to RESOLVED`() = runTest {
        sut = MainTodoListViewModel(getAllTasksUseCase)

        sut.onPermissionResult(isGranted = true)

        assertThat(sut.permissionStatus.value).isEqualTo(PermissionStatus.RESOLVED)
    }
}