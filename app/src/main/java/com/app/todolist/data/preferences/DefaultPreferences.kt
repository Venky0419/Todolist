package com.app.todolist.data.preferences

import android.content.SharedPreferences
import com.app.todolist.domain.models.Priority
import com.app.todolist.domain.models.TodoTaskModel
import com.app.todolist.domain.models.toEnum
import com.app.todolist.domain.preferences.Preferences
import com.app.todolist.utils.Constants.EMPTY_STRING

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
class DefaultPreferences(private val sharedPreferences: SharedPreferences) : Preferences {
    override fun saveSort(priority: Priority) {
        sharedPreferences.edit().putString(Preferences.KEY_SORT, priority.name).apply()
    }

    override fun loadPriority(): Priority {
        val priority = sharedPreferences.getString(
            Preferences.KEY_SORT,
            Priority.NONE.name
        )
        return priority?.toEnum() ?: Priority.NONE
    }

    override fun saveTask(todoTaskModel: TodoTaskModel) {
        sharedPreferences.edit().putInt(Preferences.KEY_TASK_ID, todoTaskModel.id).apply()

        sharedPreferences.edit().putString(Preferences.KEY_TASK_TITLE, todoTaskModel.title).apply()

        sharedPreferences.edit().putString(Preferences.KEY_TASK_DESC, todoTaskModel.description)
            .apply()

        sharedPreferences.edit().putInt(Preferences.KEY_TASK_PRIORITY, todoTaskModel.priority)
            .apply()
    }

    override fun getLastTask(): TodoTaskModel {
        return try {
            val id = sharedPreferences.getInt(Preferences.KEY_TASK_ID, -1)
            val title = sharedPreferences.getString(Preferences.KEY_TASK_TITLE, EMPTY_STRING)
            val desc = sharedPreferences.getString(Preferences.KEY_TASK_DESC, EMPTY_STRING)
            val priority = sharedPreferences.getInt(Preferences.KEY_TASK_PRIORITY, -1)
            TodoTaskModel(id, title!!, desc!!, priority)
        } catch (ex: Exception) {
            TodoTaskModel.EmptyModel
        }
    }


}