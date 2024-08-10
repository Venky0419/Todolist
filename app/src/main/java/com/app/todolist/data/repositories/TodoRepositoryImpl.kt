package com.app.todolist.data.repositories

import com.app.todolist.data.dao.TodoDao
import com.app.todolist.data.mappers.toEntity
import com.app.todolist.data.mappers.toModel
import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.repositories.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * [TodoRepository] implementation using [TodoDao] to interact with the database.
 * This class provides methods to perform CRUD operations on the tasks in the database.
 *
 * Created by P,Venkatesh on 07-Aug-24
 */
class TodoRepositoryImpl @Inject constructor(private val todoDao: TodoDao) : TodoRepository {
    override val getAllTasks: Flow<List<TodoTaskModel>>
        get() = todoDao.getAllTasks().map { entity -> entity.map { it.toModel() } }

    override fun getTaskById(taskId: Int): Flow<TodoTaskModel> = flow {
        val model = withContext(Dispatchers.IO) {
            todoDao.getTaskById(taskId).toModel()
        }
        emit(model)
    }

    override suspend fun addTask(todoTaskModel: TodoTaskModel) {
        todoDao.addTask(todoTaskModel.toEntity())
    }

    override suspend fun updateTask(todoTaskModel: TodoTaskModel) {
        todoDao.updateTask(todoTaskModel.toEntity())
    }

    override suspend fun deleteTask(todoTaskModel: TodoTaskModel) {
        todoDao.deleteTask(todoTaskModel.id)
    }

    override suspend fun deleteAll() {
        todoDao.deleteAllTasks()
    }

    override fun searchTask(query: String): Flow<List<TodoTaskModel>> {
        return todoDao.searchTasks(query).map { entity -> entity.map { it.toModel() } }
    }
}