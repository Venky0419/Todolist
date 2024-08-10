package com.app.todolist.presentation.view.todolist

import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.models.Priority
import com.app.todolist.utils.Constants.EMPTY_STRING
import com.app.todolist.utils.SearchAppBarState

/**
 * [TodoListUiState] data class for ui state of todo list screen composable function
 * @param loading [Boolean] default value false for loading state
 * @param tasks [List<TodoTaskModel>] default empty list for tasks list
 * @param sort [Priority] default NONE for sort priority state
 * @param searchAppBarText [String] default empty string for search app bar text state
 * @param appBarState [SearchAppBarState] default CLOSED for app bar state enum class
 * @param error [Throwable] default null for error state state
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
data class TodoListUiState(
    val loading: Boolean = false,
    val tasks: List<TodoTaskModel> = emptyList(),
    val sort: Priority = Priority.NONE,
    val searchAppBarText: String = EMPTY_STRING,
    val appBarState: SearchAppBarState = SearchAppBarState.CLOSED,
    val error: Throwable? = null
)
