package com.app.todolist.presentation.view.todotask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.app.todolist.domain.repositories.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by P,Venkatesh on 06-Aug-24
 *
 */
@HiltViewModel
class TodoTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val todoRepository: TodoRepository
) : ViewModel() {

}