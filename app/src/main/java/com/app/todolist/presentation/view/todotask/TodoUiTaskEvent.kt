package com.app.todolist.presentation.view.todotask

import com.app.todolist.utils.Action
import com.app.todolist.utils.UiText

/**
 * [TodoUiTaskEvent] sealed class for TodoTaskViewModel events and states and actions to be taken by view model and view layer respectively
 * @param NavigationBackEvent [Action] action to be taken when back button is clicked in the screen
 * @param ShowSnackbar [UiText] snackbar message to be shown in the screen
 *
 * Created by P,Venkatesh on 07-Aug-24
 */
sealed class TodoUiTaskEvent {
    data class NavigationBackEvent(val action: Action) : TodoUiTaskEvent()
    data class ShowSnackbar(val value: UiText) : TodoUiTaskEvent()
}