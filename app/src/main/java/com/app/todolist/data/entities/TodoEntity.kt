package com.app.todolist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.todolist.utils.Constants.TODO_TABLE_NAME

/**
 * Created by P,Venkatesh on 06-Aug-24
 *
 */
@Entity(tableName = TODO_TABLE_NAME)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Int
)
