package com.app.todolist.domain.models

import com.app.todolist.utils.Constants.EMPTY_STRING


/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
data class TodoTaskModel(
    val id: Int,
    val title: String,
    val description: String,
    val priority: Int
) {
    companion object {
        val EmptyModel = TodoTaskModel(-1, EMPTY_STRING, EMPTY_STRING, Priority.LOW.ordinal)
    }
}