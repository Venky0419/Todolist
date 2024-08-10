package com.app.todolist.presentation.view.todolist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.R
import com.app.todolist.domain.models.Priority
import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.preferences.Preferences
import com.app.todolist.domain.repositories.TodoRepository
import com.app.todolist.utils.Action
import com.app.todolist.utils.Constants.EMPTY_STRING
import com.app.todolist.utils.SearchAppBarState
import com.app.todolist.utils.UiText
import com.app.todolist.utils.toAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [TodoListViewModel] class for [TodoListComposable] screen and [TodoListUiState] class for state
 * @param savedStateHandle [SavedStateHandle] saved state handle from navigation graph
 * @param todoRepository [TodoRepository] repository for tasks
 * @param preferences [Preferences] repository for preferences
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
@HiltViewModel
class TodoListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val todoRepository: TodoRepository,
    private val preferences: Preferences
) : ViewModel() {

    /**
     * [action]
     * Get action from savedStateHandle
     * */
    private val action: Action =
        savedStateHandle.getStateFlow("action", EMPTY_STRING).value.toAction()

    /**
     * [searchAppBarState]
     * MutableStateFlow of [SearchAppBarState]
     * */
    val searchAppBarState = MutableStateFlow(SearchAppBarState.CLOSED)

    /**
     * [searchTextState]
     * MutableStateFlow of [String]
     * */
    val searchTextState = MutableStateFlow(EMPTY_STRING)

    /**
     * [sort]
     * MutableStateFlow of [Priority]
     * */
    private val sort: MutableStateFlow<Priority> =
        MutableStateFlow(preferences.loadPriority())

    /**
     * [loading]
     * MutableStateFlow of [Boolean]
     * */
    private val loading = MutableStateFlow(false)

    /**
     * [error]
     * MutableStateFlow of [Throwable]
     * */
    private val error: MutableStateFlow<Throwable?> = MutableStateFlow(null)

    private val _uiEvent = Channel<TodoUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val sortTasks = combine(todoRepository.getAllTasks, sort) { tasks, sort ->
        changePriority(tasks, sort)
    }.catch {
        error.value = it
    }

    /**
     * [changePriority]
     * @param tasks [List<TodoTaskModel>] task list from repository
     * @param priority [Priority] sort priority from preferences
     *
     * */
    fun changePriority(
        tasks: List<TodoTaskModel>,
        priority: Priority
    ): List<TodoTaskModel> {
        sort.value = priority
        preferences.saveSort(sort.value)

        return when (priority) {
            Priority.LOW -> {
                tasks.sortedBy { it.priority }
            }

            Priority.HIGH -> {
                tasks.sortedByDescending { it.priority }
            }

            else -> {
                tasks.sortedBy { it.id }
            }
        }
    }

    /**
     * [uiState]
     * combines the result of [loading], [sortTasks], [searchTextState],
     * [searchAppBarState] and [error] to a single value of [TodoListUiState] and returns it
     * @return uiState of [TodoListUiState]
     *
     * */
    val uiState: StateFlow<TodoListUiState> =
        combine(
            loading,
            sort,
            sortTasks,
            searchTextState,
            searchAppBarState
        ) { isLoading, sort, tasks, text, barState ->
            if (text.isEmpty()) {
                TodoListUiState(
                    loading = isLoading,
                    tasks = tasks,
                    sort = sort,
                    searchAppBarText = text,
                    appBarState = barState
                )
            } else {
                TodoListUiState(
                    loading = isLoading,
                    tasks = tasks.filter {
                        it.title.contains(searchTextState.value) || it.description.contains(
                            searchTextState.value
                        )
                    },
                    sort = sort,
                    searchAppBarText = text,
                    appBarState = barState
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TodoListUiState(loading = true)
        )

    init {
        showMessage()
    }

    /**
     * [showMessage]
     * show message based on action from savedStateHandle
     *  */
    private fun showMessage() {
        when (action) {
            Action.ADD -> {
                showSnackbar(R.string.add_success_message)
            }

            Action.DELETE -> {
                showSnackbar(R.string.delete_success_message, true)
            }

            Action.UPDATE -> {
                showSnackbar(R.string.update_success_message)
            }

            else -> {}
        }
    }

    /**
     * [showSnackbar]
     * @param text [Int]
     * @param showUndoAction [Boolean]
     * show snackbar with message and action based on action parameter passed to it
     * and showUndoAction parameter passed to it and returns it to view model scope
     * */
    private fun showSnackbar(text: Int, showUndoAction: Boolean = false) {
        viewModelScope.launch {
            _uiEvent.send(TodoUiEvent.ShowSnackbar(UiText.StringResource(text), showUndoAction))
        }
    }

    /**
     * [deleteAllTasks]
     * delete all tasks from database and returns it to view model scope
     * */
    fun deleteAllTasks() {
        viewModelScope.launch {
            todoRepository.deleteAll()
        }
    }

    /**
     * [selectTask]
     * @param todoTaskModel [TodoTaskModel]
     * save task in preferences
     * */
    fun selectTask(todoTaskModel: TodoTaskModel) {
        preferences.saveTask(todoTaskModel)
    }

    /**
     * [undoDeletedTask]
     * undo deleted task from database and returns it to view model scope
     * */
    fun undoDeletedTask() {
        val task = preferences.getLastTask()
        if (task.id != TodoTaskModel.EmptyModel.id) {
            viewModelScope.launch {
                todoRepository.addTask(task)
                preferences.saveTask(TodoTaskModel.EmptyModel)
            }
        }
    }

    /**
     * [searchTextChange]
     * @param text [String]
     * search state update with new text
     * */
    fun searchTextChange(text: String) {
        searchTextState.value = text
    }

    /**
     * [showSearchBar]
     * show search bar with status of search text
     * */
    fun showSearchBar() {
        searchAppBarState.value = SearchAppBarState.OPENED
    }

    /**
     * [closeSearchBar]
     * close search bar with status of search text empty or not
     * */
    fun closeSearchBar() {
        if (searchTextState.value.isEmpty()) {
            searchAppBarState.value = SearchAppBarState.CLOSED
        } else {
            searchTextChange(EMPTY_STRING)
        }
    }
}
