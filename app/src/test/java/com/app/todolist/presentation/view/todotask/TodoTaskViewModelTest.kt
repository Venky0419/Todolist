package com.app.todolist.presentation.view.todotask

import androidx.lifecycle.SavedStateHandle
import com.app.todolist.R
import com.app.todolist.domain.models.Priority
import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.repositories.TodoRepository
import com.app.todolist.presentation.view.MainDispatcherRule
import com.app.todolist.utils.Action
import com.app.todolist.utils.UiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class TodoTaskViewModelTest {
    private lateinit var viewModel: TodoTaskViewModel

    private val todoRepository: TodoRepository = mock()
    private lateinit var savedStateHandle: SavedStateHandle

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle()
    }

    @Test
    fun `getTask loads task when taskId is valid`() = runTest {
        val taskId = 1
        savedStateHandle["taskId"] = taskId
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
        savedStateHandle["taskId"] = 1
        viewModel = TodoTaskViewModel(savedStateHandle, todoRepository)
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

        verify(todoRepository).addTask(
            TodoTaskModel(0, "New Task", "New Description", Priority.NONE.ordinal)
        )
        val uiEvent = viewModel.uiEvent.first()
        assertEquals(
            TodoUiTaskEvent.NavigationBackEvent(Action.ADD),
            uiEvent
        )
    }

    @Test
    fun `addTask shows snackbarwhen task is empty`() = runTest {
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
    fun `deleteTask deletes task from repository and navigates back with DELETE action`() =
        runTest {
            val taskId = 1
            savedStateHandle["taskId"] = taskId
            val testTask =
                TodoTaskModel(taskId, "Test Task", "Test Description", Priority.LOW.ordinal)
            // Assuming you have a way to set the initial task in your FakeTodoRepository
            todoRepository.addTask(testTask)

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