package com.shekhargh.todolistultimate.worker.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.shekhargh.todolistultimate.R
import com.shekhargh.todolistultimate.data.TodoTaskItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TaskNotifierImpl @Inject constructor(@param:ApplicationContext private val context: Context) :
    TaskNotifier {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "task_master_ultimate_channel"

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    channelId,
                    "Task Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun showNotification(task: TodoTaskItem) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(task.title)
            .setContentText(task.description)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .build()
        notificationManager.notify(task.id, notification)

    }
}