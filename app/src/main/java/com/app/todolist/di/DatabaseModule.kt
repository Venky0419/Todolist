package com.app.todolist.di

import android.content.Context
import androidx.room.Room
import com.app.todolist.data.TodoAppDB
import com.app.todolist.utils.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [DatabaseModule] is a Dagger module that provides Database instance
 * and DAO instance for the application
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, TodoAppDB::class.java, DB_NAME).build()

    @Singleton
    @Provides
    fun provideDao(database: TodoAppDB) = database.todoDao()
}