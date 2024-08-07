package com.app.todolist.presentation.view.todotask

import com.app.todolist.domain.models.TodoTaskModel

/**
 * Created by P,Venkatesh on 06-Aug-24
 *
 */
data class TodoTaskUiState(
    val loading: Boolean = false,
    val todoTaskModel: TodoTaskModel? = null,
    val error: Throwable? = null
)