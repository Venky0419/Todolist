package com.app.todolist.domain.models

import com.app.todolist.utils.Constants.EMPTY_STRING


/**
 * [TodoTaskModel] is a data class representing a task in the todo list.
 * @property id [Int] - Task Id [Primary Key] of the table [todo_table] in database [TodoDatabase] in Room Database Library of Android Architecture Components.
 * @property title [String] - Title of the task.
 * @property description [String] - Description of the task.
 * @property priority [Int] - Priority of the task.
 * It also has a companion object with an empty [TodoTaskModel] model.
 *
 * Created by P,Venkatesh on 07-Aug-24
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