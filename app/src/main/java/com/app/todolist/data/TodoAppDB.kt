package com.app.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.todolist.data.dao.TodoDao
import com.app.todolist.data.entities.TodoEntity

/**
 * Created by P,Venkatesh on 06-Aug-24
 *
 */
@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class TodoAppDB : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}