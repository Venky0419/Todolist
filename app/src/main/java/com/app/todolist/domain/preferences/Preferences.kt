package com.app.todolist.domain.preferences

import com.app.todolist.domain.models.Priority
import com.app.todolist.domain.models.TodoTaskModel

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
interface Preferences {

    fun saveSort(priority: Priority)

    fun loadPriority():Priority

    fun saveTask(todoTaskModel: TodoTaskModel)

    fun getLastTask():TodoTaskModel

    companion object{
        const val KEY_SORT = "sort"
        const val KEY_TASK_ID = "task_id"
        const val KEY_TASK_TITLE = "task_title"
        const val KEY_TASK_DESC = "task_desc"
        const val KEY_TASK_PRIORITY = "task_priority"

    }
}