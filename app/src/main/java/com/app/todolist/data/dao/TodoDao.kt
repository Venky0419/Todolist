package com.app.todolist.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.todolist.data.entities.TodoEntity
import kotlinx.coroutines.flow.Flow

/**
 * [TodoDao] is the Data Access Object for the database.
 * It provides access to the database and contains the database queries.
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTasks():Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo_table WHERE id=:taskId")
    fun getTaskById(taskId:Int):TodoEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(todoEntity: TodoEntity)

    @Update
    suspend fun updateTask(todoEntity: TodoEntity)

    @Query("DELETE FROM todo_table WHERE id=:taskId")
    suspend fun deleteTask(taskId:Int)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM todo_table WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchTasks(query:String):Flow<List<TodoEntity>>


    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun searchByLowPriority():Flow<List<TodoEntity>>


    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 3 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 1 END")
    fun searchByHighPriority():Flow<List<TodoEntity>>

}