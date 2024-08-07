package com.app.todolist.presentation.view.todotask.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.todolist.R
import com.app.todolist.domain.models.Priority
import com.app.todolist.presentation.view.todolist.composable.components.PriorityItem
import com.app.todolist.ui.theme.HEIGHT_60_DP
import com.app.todolist.ui.theme.LARGE_PADDING
import com.app.todolist.ui.theme.ONE_DP
import com.app.todolist.ui.theme.PRIORITY_INDICATOR_SIZE
import com.app.todolist.utils.Constants.ANGLE_VALUE_03F
import com.app.todolist.utils.Constants.ANGLE_VALUE_0F
import com.app.todolist.utils.Constants.ANGLE_VALUE_180F
import com.app.todolist.utils.Constants.ANGLE_VALUE_1F
import com.app.todolist.utils.Constants.ANGLE_VALUE_8F
import com.app.todolist.utils.Constants.EMPTY_STRING
import com.app.todolist.utils.Constants.ICON_ALPHA_0_5
import com.app.todolist.utils.Constants.TEXT_WEIGHT_1_5

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
@Composable
fun PriorityDropDown(
    priority: Priority,
    modifier: Modifier,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) ANGLE_VALUE_180F else ANGLE_VALUE_0F,
        label = EMPTY_STRING
    )

    Row(
        modifier = modifier
            .height(HEIGHT_60_DP)
            .clickable { expanded = true }
            .border(
                width = ONE_DP,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = ANGLE_VALUE_03F)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Canvas(modifier = Modifier
            .size(PRIORITY_INDICATOR_SIZE)
            .weight(ANGLE_VALUE_1F), onDraw = {
            drawCircle(color = priority.color)
        })
        Text(
            text = priority.name, style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(ANGLE_VALUE_8F)
                .padding(start = LARGE_PADDING)
        )
        IconButton(modifier = Modifier
            .alpha(ICON_ALPHA_0_5)
            .rotate(angle)
            .weight(TEXT_WEIGHT_1_5),
            onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(id = R.string.drop_down_arrow_icon)
            )
        }

        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { PriorityItem(priority = Priority.LOW) }, onClick = {
                expanded = false
                onPrioritySelected(Priority.LOW)
            })
            DropdownMenuItem(text = { PriorityItem(priority = Priority.MEDIUM) }, onClick = {
                expanded = false
                onPrioritySelected(Priority.MEDIUM)
            })
            DropdownMenuItem(text = { PriorityItem(priority = Priority.HIGH) }, onClick = {
                expanded = false
                onPrioritySelected(Priority.HIGH)
            })

        }
    }
}

@Preview
@Composable
fun PriorityDropDownPreview() {
    PriorityDropDown(
        priority = Priority.LOW,
        modifier = Modifier.fillMaxWidth(),
        onPrioritySelected = {})
}

@Preview
@Composable
fun PriorityHighDropDownPreview() {
    PriorityDropDown(
        priority = Priority.HIGH,
        modifier = Modifier.fillMaxWidth(),
        onPrioritySelected = {})
}