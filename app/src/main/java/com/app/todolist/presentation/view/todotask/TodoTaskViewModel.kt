package com.app.todolist.presentation.view.todotask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.todolist.R
import com.app.todolist.domain.models.Priority
import com.app.todolist.domain.models.TodoTaskModel.Companion.EmptyModel
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
 * [TodoTaskViewModel] class for [TodoTaskComposable] screen and [TodoTaskUiState] class for state
 * @param savedStateHandle [SavedStateHandle] saved state handle from navigation component to get task id from
 * bundle and pass it to repository to get task from database and update uiState accordingly
 * @param todoRepository [TodoRepository] repository to get task from database and update uiState accordingly
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
@HiltViewModel
class TodoTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val todoRepository: TodoRepository
) : ViewModel() {

    /**
     * [taskId]
     * Get task id from savedStateHandle
     * */
    private val taskId: Int = checkNotNull(savedStateHandle["taskId"])

    /**
     * [uiState]
     * MutableStateFlow of [TodoTaskUiState]
     * */
    private val _uiState = MutableStateFlow(TodoTaskUiState(todoTaskModel = EmptyModel))
    val uiState: StateFlow<TodoTaskUiState> = _uiState

    /**
     * [uiEvent]
     * Channel of [TodoUiTaskEvent]
     * */
    private val _uiEvent = Channel<TodoUiTaskEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getTask()
    }

    /**
     * [getTask]
     *  Getting task from repository based on taskId and updating the
     *  uiState accordingly, if taskId is valid otherwise returning
     *  empty task model with error message
     * */
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
                    todoTaskModel = EmptyModel,
                    error = null
                )
        }
    }

    /**
     * [priorityChange]
     * @param priority [Priority]
     * Update priority in uiState and return the same with updated priority value
     */
    fun priorityChange(priority: Priority) {
        _uiState.update {
            it.copy(
                loading = false,
                todoTaskModel = it.todoTaskModel?.copy(priority = priority.ordinal),
                error = null
            )
        }
    }

    /**
     * [titleChange]
     * @param title [String]
     * Update title in uiState and return the same with updated title value
     */
    fun titleChange(title: String) {
        _uiState.update {
            it.copy(
                loading = false,
                todoTaskModel = it.todoTaskModel?.copy(title = title),
                error = null
            )
        }
    }

    /**
     * [descriptionChange]
     * @param description [String]
     * Update description in uiState and return the same with updated description value
     */
    fun descriptionChange(description: String) {
        _uiState.update {
            it.copy(
                loading = false,
                todoTaskModel = it.todoTaskModel?.copy(description = description),
                error = null
            )
        }
    }

    /**
     * [addTask]
     * Add task to repository and return to list screen with ADD action
     * if task is not empty otherwise show snackbar with error message
     * */
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

    /**
     * [updateTask]
     * update task in repository and return to list screen with UPDATE action
     * if task is not empty otherwise show snackbar with error message
     * */
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

    /**
     * [deleteTask]
     * delete task from repository and return to list screen with DELETE action
     * */
    fun deleteTask() {
        viewModelScope.launch {
            _uiState.value.todoTaskModel?.let { todoRepository.deleteTask(it) }
            returnToList(Action.DELETE)
        }
    }

    /**
     *  [showSnackBar]
     *  @param text [Int]
     *  show snackbar with error message from string resource id
     * */
    private suspend fun showSnackBar(text: Int) {
        _uiEvent.send(TodoUiTaskEvent.ShowSnackbar(UiText.StringResource(text)))
    }

    /**
     *  [returnToList]
     *  @param action [Action]
     *  return to list screen with action value as parameter
     *  */
    private suspend fun returnToList(action: Action) {
        _uiEvent.send(TodoUiTaskEvent.NavigationBackEvent(action))
    }
}
