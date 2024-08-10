package com.app.todolist.presentation.view.todolist.composable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.app.todolist.R
import com.app.todolist.ui.theme.ICON_SIZE_120
import com.app.todolist.ui.theme.TodolistTheme
import com.app.todolist.utils.Constants.ICON_ALPHA_0_5

/**
 * [EmptyContent] is a composable function that displays an empty content message when there are no tasks.
 *
 * Created by P,Venkatesh on 07-Aug-24
 */
@Composable
fun EmptyContent() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(ICON_SIZE_120)
                .alpha(ICON_ALPHA_0_5),
            painter = painterResource(id = R.drawable.ic_sad_face),
            contentDescription = stringResource(id = R.string.sad_face_icon),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(id = R.string.no_tasks_found),
            modifier = Modifier.alpha(ICON_ALPHA_0_5),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
    }
}

@Preview
@Composable
private fun EmptyContentPreview(){
    EmptyContent()
}

@Preview
@Composable
private fun EmptyContentDarkModePreview(){
    TodolistTheme(darkTheme = true) {
        EmptyContent()
    }
}
