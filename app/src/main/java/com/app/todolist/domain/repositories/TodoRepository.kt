package com.app.todolist.domain.repositories

import com.app.todolist.domain.models.TodoTaskModel
import kotlinx.coroutines.flow.Flow

/**
 * [TodoRepository] is an interface data layer
 * to handle communication with any data source such as Server or local database.
 *
 * Created by P,Venkatesh on 07-Aug-24
 */

interface TodoRepository {

    val getAllTasks:Flow<List<TodoTaskModel>>

    fun getTaskById(taskId:Int):Flow<TodoTaskModel>

    suspend fun addTask(todoTaskModel: TodoTaskModel)

    suspend fun updateTask(todoTaskModel: TodoTaskModel)

    suspend fun deleteTask(todoTaskModel: TodoTaskModel)

    suspend fun deleteAll()

    fun searchTask(query:String):Flow<List<TodoTaskModel>>
}