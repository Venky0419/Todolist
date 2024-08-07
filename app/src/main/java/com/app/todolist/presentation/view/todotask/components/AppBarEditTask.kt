package com.app.todolist.presentation.view.todotask.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.todolist.R
import com.app.todolist.ui.theme.TodolistTheme
import com.app.todolist.utils.TestTags

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarEditTask(
    onBackPressed: () -> Unit,
    onDeleteClicked: () -> Unit,
    onSaveClicked: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.testTag(TestTags.TaskScreen.TASK_DETAILS_APP_BAR),
        title = { Text(text = stringResource(id = R.string.task)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            BackAction {
                onBackPressed()
            }
        },
        actions = {
            DeleteAction {
                onDeleteClicked()
            }
            SaveAction {
                onSaveClicked()
            }
        },
    )
}



@Composable
fun DeleteAction(onDeleteClicked: () -> Unit) {
    IconButton(modifier = Modifier.testTag(TestTags.TaskScreen.DELETE_BUTTON_ACTION),
        onClick = { onDeleteClicked() }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_icon),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
fun TodoAppBarEditTaskPreview(){
    TodolistTheme {
        AppBarEditTask({},{}) {

        }
    }
}

@Preview
@Composable
fun TodoAppBarEditTaskDarkModePreview(){
    TodolistTheme(darkTheme = true) {
        AppBarEditTask({},{}) {

        }
    }
}


