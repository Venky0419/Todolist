package com.app.todolist.presentation.view.todotask

import com.app.todolist.domain.models.TodoTaskModel

/**
 * [TodoTaskUiState] data class for TodoTaskUiState in TodoTaskViewModel and TodoTaskComposable
 * @property loading [Boolean] default value false for loading state
 * @property todoTaskModel [TodoTaskModel] default value is null for todoTaskModel state
 * @property error [Throwable] default null for error state state
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
data class TodoTaskUiState(
    val loading: Boolean = false,
    val todoTaskModel: TodoTaskModel? = null,
    val error: Throwable? = null
)
