package com.app.todolist.presentation.view.todolist

import androidx.lifecycle.SavedStateHandle
import com.app.todolist.R
import com.app.todolist.domain.models.Priority
import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.preferences.Preferences
import com.app.todolist.domain.repositories.TodoRepository
import com.app.todolist.presentation.view.MainDispatcherRule
import com.app.todolist.utils.Action
import com.app.todolist.utils.Constants.EMPTY_STRING
import com.app.todolist.utils.SearchAppBarState
import com.app.todolist.utils.UiText
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModelTest {

    private lateinit var viewModel: TodoListViewModel
    private var todoRepository: TodoRepository = mock()
    private val preferences: Preferences = mock()
    private lateinit var savedStateHandle: SavedStateHandle

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle()
        viewModel = TodoListViewModel(savedStateHandle, todoRepository, preferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUiEvent on ADD action sends ShowSnackbar event`() = runTest {
        // Set the action in SavedStateHandle
        savedStateHandle["action"] = Action.ADD.name

        // Re-initialize ViewModel to reflect the change
        viewModel = TodoListViewModel(savedStateHandle, todoRepository, preferences)
        val uiEvent = viewModel.uiEvent.first()

        // Assert that the event is a ShowSnackbar event with the correct message
        assertEquals(
            TodoUiEvent.ShowSnackbar(UiText.StringResource(R.string.add_success_message), false),
            uiEvent
        )
    }

    @Test
    fun `changePriority sorts tasks correctly`() = runTest {
        val task1 = TodoTaskModel(1, "Task 1", "", Priority.HIGH.ordinal)
        val task2 = TodoTaskModel(2, "Task 2", "", Priority.LOW.ordinal)
        val tasks = listOf(task1, task2)

        // Sort by LOW priority
        val sortedLow = viewModel.changePriority(tasks, Priority.LOW)
        assertEquals(listOf(task2, task1), sortedLow)

        // Sort by HIGH priority
        val sortedHigh = viewModel.changePriority(tasks, Priority.HIGH)
        assertEquals(listOf(task1, task2), sortedHigh)

        // Sort by MEDIUM priority
        val sortedMedium = viewModel.changePriority(tasks, Priority.MEDIUM)
        assertEquals(listOf(task1, task2), sortedMedium)

        // Sort by ID (default)
        val sortedId = viewModel.changePriority(tasks, Priority.NONE)
        assertEquals(listOf(task1, task2), sortedId)
    }

    @Test
    fun `getUiState returns correct initial state`() = runTest {
        val uiState = viewModel.uiState.first()
        assertEquals(true, uiState.loading)
        assertEquals(emptyList<TodoTaskModel>(), uiState.tasks)
        assertEquals(Priority.NONE, uiState.sort)
        assertEquals(EMPTY_STRING, uiState.searchAppBarText)
        assertEquals(SearchAppBarState.CLOSED, uiState.appBarState)
    }

    @Test
    fun `deleteAllTasks calls repository deleteAll`() = runTest {
        viewModel.deleteAllTasks()
        verify(todoRepository).deleteAll()
    }

    @Test
    fun `selectTask saves task to preferences`() {
        val task = TodoTaskModel(1, "Test Task", "Test Description", Priority.LOW.ordinal)
        viewModel.selectTask(task)
        verify(preferences).saveTask(task)
    }

    @Test
    fun `undoDeletedTask adds last task from preferences and clears it`() = runTest {
        val task = TodoTaskModel(1, "Deleted Task", "", Priority.HIGH.ordinal)
        whenever(preferences.getLastTask()).thenReturn(task)
        viewModel.undoDeletedTask()
        verify(todoRepository).addTask(task)
        verify(preferences).saveTask(TodoTaskModel.EmptyModel)
    }

    @Test
    fun `searchTextChange updates searchTextState`() {
        val newText = "Search Query"
        viewModel.searchTextChange(newText)
        assertEquals(newText, viewModel.searchTextState.value)
    }

    @Test
    fun `showSearchBar updates searchAppBarState to OPENED`() {
        viewModel.showSearchBar()
        assertEquals(SearchAppBarState.OPENED, viewModel.searchAppBarState.value)
    }

    @Test
    fun `closeSearchBar updates searchAppBarState to CLOSED when search text is empty`() {
        viewModel.closeSearchBar()
        assertEquals(SearchAppBarState.CLOSED, viewModel.searchAppBarState.value)
    }

    @Test
    fun `closeSearchBar clears searchTextState when search text is not empty`() {
        viewModel.searchTextChange("Non Empty")
        viewModel.closeSearchBar()
        assertEquals(EMPTY_STRING, viewModel.searchTextState.value)
        assertEquals(SearchAppBarState.CLOSED, viewModel.searchAppBarState.value)
    }
}
