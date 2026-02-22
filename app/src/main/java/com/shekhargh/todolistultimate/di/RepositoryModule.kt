package com.shekhargh.todolistultimate.di

import android.content.Context
import androidx.room.Room
import com.shekhargh.todolistultimate.data.TASK_MASTER_DATABASE_NAME
import com.shekhargh.todolistultimate.data.TodoListUltimateDao
import com.shekhargh.todolistultimate.data.TodoListUltimateDatabase
import com.shekhargh.todolistultimate.data.repository.TodoListUltimateRepositoryImpl
import com.shekhargh.todolistultimate.domain.TodoListUltimateRepository
import com.shekhargh.todolistultimate.worker.notification.TaskNotifier
import com.shekhargh.todolistultimate.worker.notification.TaskNotifierImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsRepository(impl: TodoListUltimateRepositoryImpl): TodoListUltimateRepository

    @Binds
    @Singleton
    abstract fun bindsTaskNotifier(impl: TaskNotifierImpl): TaskNotifier

    companion object {
        @Provides
        fun providesTodoDatabase(@ApplicationContext applicationContext: Context): TodoListUltimateDatabase {
            return Room.databaseBuilder(
                applicationContext,
                TodoListUltimateDatabase::class.java,
                TASK_MASTER_DATABASE_NAME
            ).allowMainThreadQueries().build()

        }

        @Provides
        @Singleton
        fun providesDao(db: TodoListUltimateDatabase): TodoListUltimateDao {
            return db.getDao()
        }
    }


}