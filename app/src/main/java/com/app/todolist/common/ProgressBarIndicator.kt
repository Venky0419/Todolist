package com.app.todolist.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * [ProgressBarIndicator] is a composable function that displays a circular progress indicator
 * in the center of the screen.
 *
 * Created by P,Venkatesh on 07-Aug-24
 */
@Composable
fun ProgressBarIndicator() {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}