package com.app.todolist.domain.models


import androidx.compose.ui.graphics.Color
import com.app.todolist.ui.theme.HighPriorityColor
import com.app.todolist.ui.theme.MediumPriorityColor
import com.app.todolist.ui.theme.NonePriorityColor
import com.app.todolist.ui.theme.LowPriorityColor

/**
 * [Priority] priority enum with color value for each priority level NONE, LOW, MEDIUM, HIGH
 * @property color [Color] color value for each priority level
 *
 * Created by P,Venkatesh on 07-Aug-24
 */
enum class Priority(val color: Color) {
    NONE(NonePriorityColor),
    LOW(LowPriorityColor),
    MEDIUM(MediumPriorityColor),
    HIGH(HighPriorityColor),
}

/**
 * [toEnum] extension function to convert string to enum
 * @return [Priority] enum value for the string
 * */
fun String.toEnum(): Priority {
    return when (this) {
        Priority.NONE.name -> {
            Priority.NONE
        }

        Priority.LOW.name -> {
            Priority.LOW
        }

        Priority.MEDIUM.name -> {
            Priority.MEDIUM
        }

        Priority.HIGH.name -> {
            Priority.HIGH
        }

        else -> {
            Priority.NONE
        }
    }
}
