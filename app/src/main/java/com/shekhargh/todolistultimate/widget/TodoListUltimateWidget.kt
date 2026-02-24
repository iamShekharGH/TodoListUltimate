package com.shekhargh.todolistultimate.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.shekhargh.todolistultimate.data.usecase.GetAllTasksUseCase
import com.shekhargh.todolistultimate.data.usecase.GetWidgetTaskUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class TodoListUltimateWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TodoListUltimateWidget()
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun getWidgetTaskUseCase(): GetWidgetTaskUseCase
    fun getAllTasksUseCase(): GetAllTasksUseCase
}

class TodoListUltimateWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        )
        val initialTasks = entryPoint.getWidgetTaskUseCase().invoke()
        val taskFlow = entryPoint.getAllTasksUseCase().invoke()



        provideContent {
            val tasks by taskFlow.collectAsState(initial = initialTasks)
            val groupedTasks =
                tasks.sortedBy { it.dueDate }.groupBy { it.dueDate.toLocalDate().toString() }

            GlanceTheme(
                colors = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    GlanceTheme.colors
                } else {
                    WidgetColorScheme.colors
                }
            ) {
                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .appWidgetBackground()
                        .background(GlanceTheme.colors.surface)
                        .cornerRadius(16.dp)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Pending Tasks: ${tasks.size}",
                        style = TextStyle(
                            color = GlanceTheme.colors.onSurface,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = GlanceModifier.padding(bottom = 8.dp)
                    )
                    LazyColumn(modifier = GlanceModifier.defaultWeight()) {
                        groupedTasks.forEach { (dateStr, dailyTasks) ->
                            item {
                                Text(
                                    text = dateStr,
                                    modifier = GlanceModifier.padding(top = 4.dp, bottom = 4.dp),
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = GlanceTheme.colors.primary
                                    )
                                )
                            }
                            items(dailyTasks) { task ->
                                Row(verticalAlignment = Alignment.Vertical.CenterVertically) {
                                    CheckBox(
                                        checked = task.isItDone,
                                        onCheckedChange = actionRunCallback<ToggleTaskAction>(
                                            actionParametersOf(ActionParameters.Key<Int>("TASK_ID") to task.id)
                                        )
                                    )
                                    Text(
                                        text = task.title,
                                        modifier = GlanceModifier
                                            .padding(start = 8.dp)
                                            .clickable(
                                                actionStartActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        "taskmaster://task/${task.id}".toUri()
                                                    ).apply {
                                                        setPackage(context.packageName)
                                                    }
                                                )
                                            ),
                                        style = TextStyle(
                                            color = GlanceTheme.colors.onSurface
                                        )
                                    )
                                }

                            }
                        }
                    }
                }

            }
        }
    }
}
