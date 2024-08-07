package com.app.todolist.presentation.view.todolist

import com.app.todolist.utils.UiText

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
sealed class TodoUiEvent {

    data class ShowSnackbar(val value: UiText, val showUndoAction:Boolean = false):TodoUiEvent()
}