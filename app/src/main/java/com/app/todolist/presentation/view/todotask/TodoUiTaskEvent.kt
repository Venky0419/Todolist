package com.app.todolist.presentation.view.todotask

import com.app.todolist.utils.Action
import com.app.todolist.utils.UiText

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
sealed class TodoUiTaskEvent {
    data class NavigationBackEvent(val action: Action) : TodoUiTaskEvent()
    data class ShowSnackbar(val value: UiText) : TodoUiTaskEvent()
}