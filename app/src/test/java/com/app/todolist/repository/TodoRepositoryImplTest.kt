package com.app.todolist.repository

import com.app.todolist.data.dao.TodoDao
import com.app.todolist.data.entities.TodoEntity
import com.app.todolist.data.mappers.toEntity
import com.app.todolist.data.mappers.toModel
import com.app.todolist.data.repositories.TodoRepositoryImpl
import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.repositories.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
open class TodoRepositoryImplTest {
    private lateinit var todoRepository: TodoRepository
    private val todoDao: TodoDao = mock()
    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        todoRepository = TodoRepositoryImpl(todoDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllTasks returns mapped list from dao`() = runTest {
        val testEntities = listOf(
            TodoEntity(1, "Task 1", "Description 1", 0),
            TodoEntity(2, "Task 2", "Description 2", 2),
            TodoEntity(3, "Task 3", "Description 3", 3),
            TodoEntity(4, "Task 4", "Description 4", 4)
        )
        val expectedModels = testEntities.map { it.toModel() }
        doReturn(flowOf(testEntities)).whenever(todoDao).getAllTasks()
        val result = todoRepository.getAllTasks.first()

        assertEquals(expectedModels, result)
    }

    @Test
    fun `getTaskById returns mapped model from dao`() = runTest {
        val testEntity = TodoEntity(1, "Task 1", "Description 1", 0)
        val expectedModel = testEntity.toModel()
        doReturn(testEntity).whenever(todoDao).getTaskById(1)
        val result = todoRepository.getTaskById(1).first()
        assertEquals(expectedModel, result)
    }

    @Test
    fun `addTask calls dao with mapped entity`() = runTest {
        val testModel = TodoTaskModel(1, "Test Task", "Test Description", 0)
        todoRepository.addTask(testModel)
        verify(todoDao).addTask(testModel.toEntity())
    }

    @Test
    fun `updateTask calls dao with mapped entity`() = runTest {
        val testModel = TodoTaskModel(1, "Updated Task", "Updated Description", 1)
        todoRepository.updateTask(testModel)
        verify(todoDao).updateTask(testModel.toEntity())
    }

    @Test
    fun `deleteTask calls dao with correct taskId`() = runTest {
        val testModel = TodoTaskModel(1, "Task to Delete", "", 0)
        todoRepository.deleteTask(testModel)
        verify(todoDao).deleteTask(testModel.id)
    }

    @Test
    fun `deleteAll calls dao deleteAll`() = runTest {
        todoRepository.deleteAll()
        verify(todoDao).deleteAllTasks()
    }

    @Test
    fun `searchTask returns mapped list from dao`() = runTest {
        val query = "test"
        val testEntities = listOf(
            TodoEntity(1, "Task 1", "Description 1", 0),
            TodoEntity(2, "Task 2", "Description 2", 1),
            TodoEntity(3, "Task 3", "Description 3", 4),
            TodoEntity(4, "Task 4", "Description 4", 4)
        )
        val expectedModels = testEntities.map { it.toModel() }
        doReturn(flowOf(testEntities))
            .whenever(todoDao).searchTasks(query)
        val result = todoRepository.searchTask(query).first()
        assertEquals(expectedModels, result)
    }
}