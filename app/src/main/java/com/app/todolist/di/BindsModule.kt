package com.app.todolist.di

import com.app.todolist.data.repositories.TodoRepositoryImpl
import com.app.todolist.domain.repositories.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * [BindsModule] is a Dagger module that provides bindings for interfaces.
 *
 * Created by P,Venkatesh on 07-Aug-24
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    @Binds
    abstract fun provideTodoRepository(todoRepositoryImpl: TodoRepositoryImpl): TodoRepository
}