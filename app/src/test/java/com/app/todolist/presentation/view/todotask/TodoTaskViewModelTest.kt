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
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TodoTaskViewModelTest :TodoRepositoryImplTest() {
    private lateinit var viewModel: TodoTaskViewModel

    private val todoRepository: TodoRepository = mock()
    private var savedStateHandle: SavedStateHandle = mock()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        whenever(savedStateHandle.get<Int>("taskId")).thenReturn(1)
        //every { savedStateHandle.get<Int>("taskId") } returns 1
        viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
    }

    @Test
    fun `getTask loads task when taskId is valid`() = runTest {
        val taskId = 1
        val testData = TodoTaskModel(taskId, "Test Task", "Test Description", Priority.LOW.ordinal)
        todoRepository.getTaskById(taskId)
        viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
        val uiState = viewModel.uiState.first()
        assertEquals(true, uiState.loading)
        assertEquals(testData, uiState.todoTaskModel)
        assertEquals(null, uiState.error)
    }

    @Test
    fun `getTask sets empty task when taskId is invalid`() = runTest {
        savedStateHandle["taskId"] = -1
        viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
        val uiState = viewModel.uiState.first()
        assertEquals(false, uiState.loading)
        assertEquals(TodoTaskModel.EmptyModel, uiState.todoTaskModel)
        assertEquals(null, uiState.error)
    }

    @Test
    fun `priorityChange updates task priority`() {
        savedStateHandle["taskId"] = 1
        viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
        viewModel.priorityChange(Priority.HIGH)
        val uiState = viewModel.uiState.value
        assertEquals(Priority.HIGH.ordinal, uiState.todoTaskModel?.priority)
    }

    @Test
    fun `titleChange updates task title`() {
        savedStateHandle["taskId"] = 1
        viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
        val newTitle = "New Title"
        viewModel.titleChange(newTitle)

        val uiState = viewModel.uiState.value
        assertEquals(newTitle, uiState.todoTaskModel?.title)

    }

    @Test
    fun `descriptionChange updates task description`() = runTest {
        val newDescription = "New Description"
        viewModel.descriptionChange(newDescription)

        val uiState = viewModel.uiState.value
        assertEquals(newDescription, uiState.todoTaskModel?.description)
    }

    @Test
    fun `addTask adds task to repository and navigates back with ADD action`() = runTest {
        savedStateHandle["taskId"] = -1

        viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
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
    fun `addTask shows snackbar when task is empty`() = runTest {
        savedStateHandle["taskId"] = -1
        viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
        viewModel.addTask()
        val uiEvent = viewModel.uiEvent.first()
        assertEquals(
            TodoUiTaskEvent.ShowSnackbar(UiText.StringResource(R.string.empty_task_message)),
            uiEvent
        )
    }

    @Test
    fun `updateTask updates task in repository and navigates back`() = runTest {
        // Setup
        savedStateHandle["taskId"] = 1
        viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
        val existingTask = TodoTaskModel(1, "Existing Task", "Existing Description", Priority.MEDIUM.ordinal)
        viewModel.uiState.value.copy(todoTaskModel = existingTask)

        val events = mutableListOf<TodoUiTaskEvent>()
        viewModel.uiEvent.toList(events)

        // Action
        viewModel.updateTask()

        // Verification
        verify(todoRepository).updateTask(existingTask)
        assertEquals(1, events.size)
        assertEquals(TodoUiTaskEvent.NavigationBackEvent(Action.UPDATE), events[0])
    }

    @Test
    fun `updateTask shows snackbar for empty task`() = runTest {
        savedStateHandle["taskId"] = -1
        viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
        // Setup
        val emptyTask = TodoTaskModel(1, "", "", Priority.MEDIUM.ordinal) // Empty title or description
        viewModel.uiState.value.copy(todoTaskModel = emptyTask)

        val events = mutableListOf<TodoUiTaskEvent>()
        viewModel.uiEvent.toList(events)

        // Action
        viewModel.updateTask()

        // Verification
        assertEquals(1, events.size)
        assertEquals(
            TodoUiTaskEvent.ShowSnackbar(UiText.StringResource(R.string.empty_task_message)),
            events[0]
        )
    }

    @Test
    fun `deleteTask deletes task from repository and navigates back with DELETE action`() =
        runTest {
            val taskId = 1
            savedStateHandle["taskId"] = taskId
            val testTask =
                TodoTaskModel(taskId, "Test Task", "Test Description", Priority.LOW.ordinal)
            viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
            // Action
            viewModel.deleteTask()
            // Verification
            verify(todoRepository).deleteTask(testTask)
            val uiEvent = viewModel.uiEvent.first()
            assertEquals(
                TodoUiTaskEvent.NavigationBackEvent(Action.DELETE),
                uiEvent
            )
        }
}