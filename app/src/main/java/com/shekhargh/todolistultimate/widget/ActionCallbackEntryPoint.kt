package com.shekhargh.todolistultimate.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.shekhargh.todolistultimate.data.usecase.GetTaskByIdUseCase
import com.shekhargh.todolistultimate.data.usecase.UpdateTaskUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ActionCallbackEntryPoint {
    fun getTaskByIdUseCase(): GetTaskByIdUseCase
    fun updateTaskUseCase(): UpdateTaskUseCase
}

class ToggleTaskAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val taskId = parameters[ActionParameters.Key<Int>("TASK_ID")] ?: return

        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            ActionCallbackEntryPoint::class.java
        )

        val getTaskByIdUseCase = entryPoint.getTaskByIdUseCase()
        val updateTaskUseCase = entryPoint.updateTaskUseCase()

        val task = getTaskByIdUseCase(taskId) ?: return
        val updatedTask = task.copy(isItDone = !task.isItDone)

        updateTaskUseCase(updatedTask)
//        delay(1000)
//        TodoListUltimateWidget().updateAll(context)
    }
}