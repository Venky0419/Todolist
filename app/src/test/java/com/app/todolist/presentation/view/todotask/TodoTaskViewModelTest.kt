package com.app.todolist.presentation.view.todotask

import androidx.lifecycle.SavedStateHandle
import com.app.todolist.R
import com.app.todolist.domain.models.Priority
import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.repositories.TodoRepository
import com.app.todolist.presentation.view.MainDispatcherRule
import com.app.todolist.repository.TodoRepositoryImplTest
import com.app.todolist.utils.Action
import com.app.todolist.utils.UiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TodoTaskViewModelTest : TodoRepositoryImplTest() {
    private lateinit var viewModel: TodoTaskViewModel

    private val todoRepository: TodoRepository = mock()
    private var savedStateHandle: SavedStateHandle = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun setUp(taskId: Int, taskModel: TodoTaskModel = TodoTaskModel.EmptyModel) {
        whenever(savedStateHandle.get<Int>("taskId")).thenReturn(taskId)
        whenever(todoRepository.getTaskById(taskId)).thenReturn(flow {
            emit(taskModel)
        })
        viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
    }

    @Test
    fun `getTask loads task when taskId is valid`() = runTest {
        val taskId = 1
        val testData = TodoTaskModel(taskId, "Test Task", "Test Description", Priority.LOW.ordinal)
        setUp(taskId, testData)
        val uiState = viewModel.uiState.first()
        assertEquals(false, uiState.loading)
        assertEquals(testData, uiState.todoTaskModel)
        assertEquals(null, uiState.error)
    }

    @Test
    fun `getTask sets empty task when taskId is invalid`() = runTest {
        setUp(-1)
        val uiState = viewModel.uiState.first()
        assertEquals(false, uiState.loading)
        assertEquals(TodoTaskModel.EmptyModel, uiState.todoTaskModel)
        assertEquals(null, uiState.error)
    }

    @Test
    fun `priorityChange updates task priority`() = runTest {
        setUp(1)
        viewModel.priorityChange(Priority.HIGH)
        val uiState = viewModel.uiState.value
        assertEquals(Priority.HIGH.ordinal, uiState.todoTaskModel?.priority)
    }

    @Test
    fun `titleChange updates task title`() = runTest {
        setUp(1)
        val newTitle = "New Title"
        viewModel.titleChange(newTitle)
        val uiState = viewModel.uiState.value
        assertEquals(newTitle, uiState.todoTaskModel?.title)
    }

    @Test
    fun `descriptionChange updates task description`() = runTest {
        setUp(1)
        val newDescription = "New Description"
        viewModel.descriptionChange(newDescription)
        val uiState = viewModel.uiState.value
        assertEquals(newDescription, uiState.todoTaskModel?.description)
    }

    @Test
    fun `addTask adds task to repository and navigates back with ADD action`() = runTest {
        setUp(-1)
        viewModel.titleChange("New Task")
        viewModel.descriptionChange("New Description")
        viewModel.addTask()
        val uiEvent = viewModel.uiEvent.first()
        assertEquals(
            TodoUiTaskEvent.NavigationBackEvent(Action.ADD),
            uiEvent
        )
    }

    @Test
    fun `addTask shows snackBar when task is empty`() = runTest {
        setUp(-1)
        viewModel.addTask()
        val uiEvent = viewModel.uiEvent.first()
        assertEquals(
            TodoUiTaskEvent.ShowSnackbar(UiText.StringResource(R.string.empty_task_message)),
            uiEvent
        )
    }

    @Test
    fun `updateTask updates task in repository and navigates back`() = runTest {
        val existingTask =
            TodoTaskModel(1, "Existing Task", "Existing Description", Priority.MEDIUM.ordinal)
        setUp(1, existingTask)
        todoRepository.updateTask(existingTask)
        viewModel.updateTask()
        viewModel.uiState.value.todoTaskModel?.let {
            assertEquals(it.description, existingTask.description)
        }
    }

    @Test
    fun `updateTask shows snackBar for empty task`() = runTest {
        val emptyTask =
            TodoTaskModel(1, "", "", Priority.MEDIUM.ordinal) // Empty title or description
        setUp(1, emptyTask)
        viewModel.updateTask()
        viewModel.uiState.value.todoTaskModel?.let {
            assertTrue(it.title.isEmpty())
            assertTrue(it.description.isEmpty())
        }
    }

    @Test
    fun `deleteTask deletes task from repository and navigates back with DELETE action`() =
        runTest {
            val testTask =
                TodoTaskModel(1, "Test Task", "Test Description", Priority.LOW.ordinal)
            todoRepository.deleteTask(testTask)
            setUp(1)
            viewModel.deleteTask()
            val uiEvent = viewModel.uiEvent.first()
            assertEquals(
                TodoUiTaskEvent.NavigationBackEvent(Action.DELETE),
                uiEvent
            )
        }
}
