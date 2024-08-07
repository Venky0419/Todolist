package com.app.todolist.presentation.view.todolist.composable.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.models.Priority
import com.app.todolist.ui.theme.LARGE_PADDING
import com.app.todolist.ui.theme.PRIORITY_INDICATOR_SIZE
import com.app.todolist.ui.theme.TEXT_WEIGHT_8
import com.app.todolist.ui.theme.TodolistTheme
import com.app.todolist.utils.TestTags

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
@Composable
fun ListTasks(
    tasks: List<TodoTaskModel> = emptyList(),
    innerPadding: PaddingValues,
    listState: LazyListState,
    onTasksSelected: (TodoTaskModel) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .testTag(TestTags.ListScreen.TASKS_LIST)
    ) {
        items(items = tasks, key = { it.id }) { task ->
            TaskItem(todoTaskModel = task, onTasksSelected = onTasksSelected)
        }
    }
}

@Composable
fun TaskItem(todoTaskModel: TodoTaskModel, onTasksSelected: (TodoTaskModel) -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RectangleShape, onClick = {
        onTasksSelected(todoTaskModel)
    }
    ) {
        Column(
            modifier = Modifier
                .padding(all = LARGE_PADDING)
                .fillMaxWidth()
        ) {
            Row {
                todoTaskModel.title.let {
                    Text(
                        modifier = Modifier.weight(TEXT_WEIGHT_8),
                        text = it,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.TopEnd
                    ) {

                        Canvas(
                            modifier = Modifier
                                .width(PRIORITY_INDICATOR_SIZE)
                                .height(
                                    PRIORITY_INDICATOR_SIZE
                                )
                        ) {
                            drawCircle(color = Priority.entries[todoTaskModel.priority].color)
                        }
                    }
                }
            }
            
            Text(
                text = todoTaskModel.description,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)
        }
    }
    
    
}

@Preview
@Composable
fun TaskItemsPreview(){
    TaskItem(todoTaskModel = TodoTaskModel(
        id = 1,
        title = "Test Task",
        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged",
        priority = Priority.HIGH.ordinal
    )
    ) {
        
    }
}

@Preview
@Composable
fun TaskItemsDarkModePreview(){
    TodolistTheme(darkTheme = true) {
        TaskItem(todoTaskModel = TodoTaskModel(
            id = 1,
            title = "Test Task",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged",
            priority = Priority.HIGH.ordinal
        )
        ) {

        }
    }
}