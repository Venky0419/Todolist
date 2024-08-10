package com.app.todolist.presentation.view.todolist

import com.app.todolist.utils.UiText

/**
 * [TodoUiEvent] sealed class for UI events to be handled by view model
 * @see ShowSnackbar for showing snackbar with error message and showUndoAction for showing undo action in snackbar when deleting task
 *
 * Created by P,Venkatesh on 07-Aug-24
 */
sealed class TodoUiEvent {

    data class ShowSnackbar(val value: UiText, val showUndoAction:Boolean = false):TodoUiEvent()
}