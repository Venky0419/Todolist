package com.app.todolist.presentation.view.todolist

import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.models.Priority
import com.app.todolist.utils.Constants.EMPTY_STRING
import com.app.todolist.utils.SearchAppBarState

/**
 * Created by P,Venkatesh on 06-Aug-24
 *
 */
data class TodoListUiState(
    val loading: Boolean = false,
    val tasks: List<TodoTaskModel> = emptyList(),
    val sort: Priority = Priority.NONE,
    val searchAppBarText: String = EMPTY_STRING,
    val appBarState: SearchAppBarState = SearchAppBarState.CLOSED,
    val error: Throwable? = null
)