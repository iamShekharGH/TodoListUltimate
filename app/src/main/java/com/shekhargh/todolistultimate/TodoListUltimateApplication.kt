package com.shekhargh.todolistultimate

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.shekhargh.todolistultimate.worker.DailySummaryWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class TodoListUltimateApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()


    override fun onCreate() {
        super.onCreate()

        val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()

        val workRequest = PeriodicWorkRequestBuilder<DailySummaryWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        ).setConstraints(constraints).setInitialDelay(duration = 10, timeUnit = TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                uniqueWorkName = "daily_task_reminder",
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
                request = workRequest
            )

        val workRequestOneTime = OneTimeWorkRequestBuilder<DailySummaryWorker>().build()
        WorkManager.getInstance(this)
            .enqueueUniqueWork("ot", ExistingWorkPolicy.REPLACE, workRequestOneTime)


    }
}