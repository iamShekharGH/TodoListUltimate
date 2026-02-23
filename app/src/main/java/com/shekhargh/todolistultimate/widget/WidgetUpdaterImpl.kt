package com.shekhargh.todolistultimate.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.shekhargh.todolistultimate.domain.WidgetUpdater
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WidgetUpdaterImpl @Inject constructor(
    @param:ApplicationContext val context: Context
) : WidgetUpdater {
    override suspend fun updateWidget() {
        TodoListUltimateWidget().updateAll(context)
    }
}