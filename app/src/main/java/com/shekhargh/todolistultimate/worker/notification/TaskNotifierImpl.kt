package com.shekhargh.todolistultimate.worker.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
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
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "taskmaster://task/${task.id}".toUri()
        )
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            getPendingIntent(task.id, flags)
        }
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(task.title)
            .setContentText(task.description)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(task.id, notification)

    }

    override fun showNotificationSummary(list: List<TodoTaskItem>) {
        val summary = list.joinToString(separator = "\n") { "• ${it.title}" }
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Pending tasks (${list.size}):~")
            .setContentText("You have ${list.size} tasks left")
            .setStyle(NotificationCompat.BigTextStyle().bigText(summary))
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .build()
        notificationManager.notify(1001, notification)
    }

}