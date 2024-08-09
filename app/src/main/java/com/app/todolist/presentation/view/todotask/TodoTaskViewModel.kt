package com.app.todolist.presentation.view.todotask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.R
import com.app.todolist.domain.models.Priority
import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.repositories.TodoRepository
import com.app.todolist.utils.Action
import com.app.todolist.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private val taskId: Int = checkNotNull(savedStateHandle["taskId"])

    private val _uiState = MutableStateFlow(TodoTaskUiState())
    val uiState: StateFlow<TodoTaskUiState> = _uiState

    private val _uiEvent = Channel<TodoUiTaskEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getTask()
    }

    private fun getTask() {
        if (taskId > -1) {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                viewModelScope.launch {
                    todoRepository.getTaskById(taskId).collect {
                        _uiState.value = _uiState.value.copy(loading = false, todoTaskModel = it)
                    }
                }
            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(loading = false, todoTaskModel = null, error = e)
            }
        } else {
            _uiState.value =
                _uiState.value.copy(
                    loading = false,
                    todoTaskModel = TodoTaskModel.EmptyModel,
                    error = null
                )
        }
    }

    fun priorityChange(priority: Priority) {
        _uiState.update {
            it.copy(
                loading = false,
                todoTaskModel = it.todoTaskModel?.copy(priority = priority.ordinal),
                error = null
            )
        }
    }

    fun titleChange(title: String) {
            _uiState.update {
                it.copy(
                    loading = false,
                    todoTaskModel = it.todoTaskModel?.copy(title = title),
                    error = null
                )
            }
    }


    fun descriptionChange(description: String) {
        _uiState.update {
            it.copy(
                loading = false,
                todoTaskModel = it.todoTaskModel?.copy(description = description),
                error = null
            )
        }
    }

    fun addTask() {
        if (_uiState.value.todoTaskModel?.title?.isEmpty() == true ||
            _uiState.value.todoTaskModel?.description?.isEmpty() == true
        ) {
            viewModelScope.launch {
                showSnackBar(R.string.empty_task_message)
            }
        } else {
            viewModelScope.launch {
                _uiState.value.todoTaskModel?.copy(id = 0)?.let { todoRepository.addTask(it) }
                returnToList(Action.ADD)
            }
        }
    }


    fun updateTask() {
        if (_uiState.value.todoTaskModel?.title?.isEmpty() == true ||
            _uiState.value.todoTaskModel?.description?.isEmpty() == true
        ) {
            viewModelScope.launch {
                showSnackBar(R.string.empty_task_message)
            }
        } else {
            viewModelScope.launch {
                _uiState.value.todoTaskModel?.let { todoRepository.updateTask(it) }
                returnToList(Action.UPDATE)
            }
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            _uiState.value.todoTaskModel?.let { todoRepository.deleteTask(it) }
            returnToList(Action.DELETE)
        }
    }


    private suspend fun showSnackBar(text: Int) {
        _uiEvent.send(TodoUiTaskEvent.ShowSnackbar(UiText.StringResource(text)))
    }

    private suspend fun returnToList(action: Action) {
        _uiEvent.send(TodoUiTaskEvent.NavigationBackEvent(action))
    }
}