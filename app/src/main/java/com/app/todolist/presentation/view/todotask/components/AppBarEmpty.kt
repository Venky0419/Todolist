package com.app.todolist.presentation.view.todotask.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.app.todolist.utils.Constants.EMPTY_STRING
import com.app.todolist.utils.TestTags

/**
 * [AppBarEmpty] fun to show empty app bar with back button and no title text
 * @param onBackPressed [() -> Unit] lambda function to handle back button click
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarEmpty(onBackPressed:()-> Unit){
    TopAppBar(
        modifier = Modifier.testTag(TestTags.TaskScreen.TASK_DETAILS_APP_BAR),
        title = { Text(text = EMPTY_STRING) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            BackAction {
                onBackPressed()
            }
        }
    )
}