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
 * Created by P,Venkatesh on 06-Aug-24
 *
 */
@HiltViewModel
class TodoListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val todoRepository: TodoRepository,
    private val preferences: Preferences
) : ViewModel() {

    private val action: Action =
        savedStateHandle.getStateFlow("action", EMPTY_STRING).value.toAction()

    init {
        showMessage()
    }

    private val searchAppBarState = MutableStateFlow(SearchAppBarState.CLOSED)

    private val searchTextState = MutableStateFlow(EMPTY_STRING)

    private val sort: MutableStateFlow<Priority> =
        MutableStateFlow(preferences.loadPriority())

    private val loading = MutableStateFlow(false)

    private val error: MutableStateFlow<Throwable?> = MutableStateFlow(null)

    private val _uiEvent = Channel<TodoUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val sortTasks = combine(todoRepository.getAllTasks, sort) { tasks, sort ->
        changePriority(tasks, sort)
    }.catch {
        error.value = it
    }

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

    private fun showMessage() {
        when (action) {
            Action.ADD -> {
                showSnackbar(R.string.add_success_message)
            }

            Action.DELETE -> {
                showSnackbar(R.string.delete_success_message)
            }

            Action.UPDATE -> {
                showSnackbar(R.string.update_success_message)
            }

            else -> {}
        }
    }

    fun showSnackbar(text: Int, showUndoAction: Boolean = false) {
        viewModelScope.launch {
            _uiEvent.send(TodoUiEvent.ShowSnackbar(UiText.StringResource(text), showUndoAction))
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            todoRepository.deleteAll()
        }
    }

    fun selectTask(todoTaskModel: TodoTaskModel) {
        preferences.saveTask(todoTaskModel)
    }

    fun undoDeletedTask() {
        val task = preferences.getLastTask()
        if (task.id != TodoTaskModel.EmptyModel.id) {
            viewModelScope.launch {
                todoRepository.addTask(task)
                preferences.saveTask(TodoTaskModel.EmptyModel)
            }
        }
    }

    fun searchTextChange(text: String) {
        searchTextState.value = text
    }

    fun showSearchBar(){
        searchAppBarState.value = SearchAppBarState.OPENED
    }

    fun closeSearchBar() {
        if (searchTextState.value.isEmpty()) {
            searchAppBarState.value = SearchAppBarState.CLOSED
        } else {
            searchTextChange(EMPTY_STRING)
        }
    }
}

