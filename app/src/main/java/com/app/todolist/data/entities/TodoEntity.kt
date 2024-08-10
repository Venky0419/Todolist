package com.app.todolist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.todolist.utils.Constants.TODO_TABLE_NAME

/**
 * [TodoEntity] is a data class that represents a todoTask item.
 * It has a primary key [id],a [title], a [description], and a [priority].
 * @property id The unique identifier for the todoTask.
 * @property title The title of the todoTask.
 * @property description The description of the todoTask.
 * @property priority The priority of the todoTask.
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
@Entity(tableName = TODO_TABLE_NAME)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Int
)
