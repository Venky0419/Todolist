package com.app.todolist.presentation.view.todotask.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.todolist.R
import com.app.todolist.domain.models.Priority
import com.app.todolist.presentation.view.todolist.TodoListUiState
import com.app.todolist.presentation.view.todotask.TodoTaskViewModel
import com.app.todolist.presentation.view.todotask.TodoUiTaskEvent
import com.app.todolist.presentation.view.todotask.components.AppBarEditTask
import com.app.todolist.presentation.view.todotask.components.AppBarEmpty
import com.app.todolist.presentation.view.todotask.components.AppBarNewTask
import com.app.todolist.presentation.view.todotask.components.PriorityDropDown
import com.app.todolist.ui.theme.LARGE_PADDING
import com.app.todolist.utils.Action
import kotlinx.coroutines.launch

/**
 * [TodoTaskComposable] TodoTaskComposable is a Composable function that is used to display the todo task screen.
 * @param viewModel [TodoTaskViewModel] = hiltViewModel() function to get the view model instance
 * @param navigateToList [() -> Unit] lambda function to navigate to list screen with action parameter passed to it
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
@Composable
fun TodoTaskComposable(
    viewModel: TodoTaskViewModel = hiltViewModel(),
    navigateToList: (Action) -> Unit
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
                is TodoUiTaskEvent.NavigationBackEvent -> {
                    navigateToList(event.action)
                    keyboardController?.hide()
                }

                is TodoUiTaskEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.value.asString(context))
                        keyboardController?.hide()
                    }
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
        if (state.loading) {
            AppBarEmpty(onBackPressed = { navigateToList(Action.NO_ACTION) })
        } else {
            if (state.todoTaskModel != null) {
                if (state.todoTaskModel!!.id == -1) {
                    AppBarNewTask(onBackPressed = { navigateToList(Action.NO_ACTION) }) {
                        viewModel.addTask()
                    }
                } else {
                    AppBarEditTask(
                        onBackPressed = { navigateToList(Action.NO_ACTION) },
                        onDeleteClicked = { viewModel.deleteTask() }) {
                        viewModel.updateTask()
                    }
                }
            }
        }
    }) { paddingValues ->
        if (state.loading) {
            CircularProgressIndicator()
        } else {
            if (state.todoTaskModel != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = LARGE_PADDING, top = LARGE_PADDING),
                        value = state.todoTaskModel!!.title,
                        label = { Text(text = stringResource(id = R.string.title)) },
                        singleLine = true,
                        onValueChange = { viewModel.titleChange(it) }
                    )

                    PriorityDropDown(
                        priority = Priority.entries[state.todoTaskModel!!.priority],
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = LARGE_PADDING),
                        onPrioritySelected = { viewModel.priorityChange(it) }
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = LARGE_PADDING),
                        value = state.todoTaskModel!!.description,
                        label = { Text(text = stringResource(id = R.string.description)) },
                        onValueChange = { viewModel.descriptionChange(it) }
                    )
                }
            }
        }

    }
}