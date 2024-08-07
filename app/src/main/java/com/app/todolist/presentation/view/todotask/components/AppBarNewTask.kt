package com.app.todolist.presentation.view.todotask.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
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
fun AppBarNewTask(onBackPressed: () -> Unit, onSaveClicked: () -> Unit) {
    TopAppBar(
        modifier = Modifier.testTag(TestTags.TaskScreen.TASK_DETAILS_APP_BAR),
        title = { Text(text = stringResource(id = R.string.new_task)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),navigationIcon = {
            BackAction {
                onBackPressed()
            }
        },
        actions = {
            SaveAction {
                onSaveClicked()
            }
        },
    )
}

@Composable
fun BackAction(onBackPressed: () -> Unit) {
    IconButton(modifier = Modifier.testTag(TestTags.TaskScreen.BACK_BUTTON_ACTION),
        onClick = { onBackPressed() }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back_arrow_icon),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun SaveAction(onSaveClicked: () -> Unit) {
    IconButton(modifier = Modifier.testTag(TestTags.TaskScreen.SAVE_BUTTON_ACTION),
        onClick = { onSaveClicked() }) {
        Icon(
            imageVector = Icons.Filled.Done,
            contentDescription = stringResource(id = R.string.OK_icon),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
fun TodoAppBarNewTaskPreview(){
    TodolistTheme {
        AppBarNewTask(onBackPressed = { /*TODO*/ }) {

        }
    }
}

@Preview
@Composable
fun TodoAppBarNewTaskDarkModePreview(){
    TodolistTheme(darkTheme = true) {
        AppBarNewTask(onBackPressed = { /*TODO*/ }) {

        }
    }
}

