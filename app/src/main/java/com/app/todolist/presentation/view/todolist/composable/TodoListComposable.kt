package com.app.todolist.presentation.view.todolist.composable

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.todolist.R
import com.app.todolist.common.ProgressBarIndicator
import com.app.todolist.domain.models.Priority
import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.presentation.view.todolist.TodoListUiState
import com.app.todolist.presentation.view.todolist.TodoListViewModel
import com.app.todolist.presentation.view.todolist.TodoUiEvent
import com.app.todolist.presentation.view.todolist.composable.components.DefaultAppBar
import com.app.todolist.presentation.view.todolist.composable.components.EmptyContent
import com.app.todolist.presentation.view.todolist.composable.components.ListTasks
import com.app.todolist.presentation.view.todolist.composable.components.SearchAppBar
import com.app.todolist.ui.theme.TodolistTheme
import com.app.todolist.utils.Constants.FAB_VALUE
import com.app.todolist.utils.SearchAppBarState
import com.app.todolist.utils.TestTags.ListScreen.FAB_BUTTON
import com.app.todolist.utils.UiText
import kotlinx.coroutines.launch

/**
 * [TodoListComposable] TodoList Composable function to show the list of tasks.
 * @param viewModel [TodoListViewModel] view model to get the data from repository
 * @param onClickTask [() -> Unit] callback to navigate to task details screen
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
@Composable
fun TodoListComposable(
    viewModel: TodoListViewModel = hiltViewModel(), onClickTask: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TodoUiEvent.ShowSnackbar -> {
                    scope.launch {
                        if (event.showUndoAction) {
                            val result =
                                snackbarHostState.showSnackbar(
                                    event.value.asString(context),
                                    actionLabel = UiText.StringResource(R.string.undo)
                                        .asString(context)
                                )
                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    viewModel.undoDeletedTask()
                                }

                                SnackbarResult.Dismissed -> {

                                }

                                else -> {}
                            }
                        } else {
                            snackbarHostState.showSnackbar(
                                event.value.asString(context)
                            )
                        }
                        keyboardController?.hide()
                    }
                }
            }

        }
    }

    TodoListContent(
        state,
        snackbarHostState,
        { viewModel.showSearchBar() },
        { viewModel.changePriority(state.tasks, it) },
        { viewModel.deleteAllTasks() },
        { viewModel.searchTextChange(it) },
        { viewModel.closeSearchBar() },
        { viewModel.selectTask(it) }) { taskId ->
        onClickTask(taskId)
    }

}

@Composable
fun TodoListContent(
    state: TodoListUiState,
    snackbarHostState: SnackbarHostState,
    onSearchBarClicked: () -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onTaskSelected: (TodoTaskModel) -> Unit,
    onFabClicked: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    //Fab is expanded initially, once the first item is visible we collapse the fab.
    //use remember derived state to avoid the unwanted compositions
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    val showAlertMessage = remember {
        mutableStateOf(false)
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (state.appBarState == SearchAppBarState.CLOSED) {
                DefaultAppBar(onOpenSearchBarClicked = { onSearchBarClicked() },
                    onPriorityChanged = { onPriorityChanged(it) },
                    onDeleteAllClicked = { showAlertMessage.value = true })
            } else {
                SearchAppBar(text = state.searchAppBarText,
                    onTextChange = { onTextChanged(it) },
                    onCloseClicked = { onCloseClicked() },
                    onSearchClicked = {})
            }
        }, floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier.testTag(FAB_BUTTON),
                text = { Text(text = stringResource(id = R.string.new_task)) },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = stringResource(id = R.string.add_button)
                    )
                },
                onClick = { onFabClicked(FAB_VALUE) },
                expanded = expandedFab
            )
        }) { innerPadding ->
        if (showAlertMessage.value) {
            AlertDialog(onDismissRequest = { showAlertMessage.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        showAlertMessage.value = false
                        onDeleteAllClicked()
                    }) {
                        Text(text = stringResource(id = R.string.comfirm))
                    }
                }, dismissButton = {
                    TextButton(onClick = {
                        showAlertMessage.value = false
                    }) {
                        Text(text = stringResource(id = R.string.dismiss))
                    }
                }, icon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.delete_icon)
                    )
                }, title = {
                    Text(text = stringResource(id = R.string.confirm_action))
                }, text = {
                    Text(text = stringResource(id = R.string.alert_dialog_text))
                })
        }

        if (state.loading) {
            ProgressBarIndicator()
        } else {
            if (state.tasks.isEmpty()) {
                EmptyContent()
            } else {
                ListTasks(innerPadding = innerPadding,
                    listState = listState,
                    tasks = state.tasks,
                    onTasksSelected = {
                        onTaskSelected(it)
                        onFabClicked(it.id)
                    })
            }
        }
    }
}

@Preview
@Composable
private fun TodoContentPreview(){
    TodoListContent(
        state = TodoListUiState(),
        snackbarHostState = SnackbarHostState(),
        onSearchBarClicked = {},
        onPriorityChanged = {},
        onDeleteAllClicked = {},
        onTextChanged = {},
        onCloseClicked = {},
        onTaskSelected = {}
    ) {}
}

@Preview
@Composable
private fun TodoContentDarkModePreview(){
    TodolistTheme(darkTheme = true) {
        TodoListContent(
            state = TodoListUiState(),
            snackbarHostState = SnackbarHostState(),
            onSearchBarClicked = {},
            onPriorityChanged = {},
            onDeleteAllClicked = {},
            onTextChanged = {},
            onCloseClicked = {},
            onTaskSelected = {}
        ) {}
    }
}
