package com.app.todolist.utils

/**
 * Created by P,Venkatesh on 06-Aug-24
 *
 */
enum class Action {
    ADD,
    UPDATE,
    DELETE,
    NO_ACTION
}

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