package com.app.todolist.utils

/**
 * [Action] enum class for handling the actions in the app such as add, update, delete and no action
 * Created by P,Venkatesh on 06-Aug-24
 *
 */
enum class Action {
    ADD,
    UPDATE,
    DELETE,
    NO_ACTION
}

/**
 * [toAction] extension function to convert string to action
 *
 */
fun String?.toAction():Action {
    return when{
        this == "ADD" -> {
            Action.ADD
        }

        this == "UPDATE" -> {
            Action.UPDATE
        }

        this == "DELETE" -> {
            Action.DELETE
        }

        else -> {
            Action.NO_ACTION
        }
    }
}