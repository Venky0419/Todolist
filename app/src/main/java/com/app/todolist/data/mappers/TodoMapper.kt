package com.app.todolist.data.mappers

import com.app.todolist.data.entities.TodoEntity
import com.app.todolist.domain.models.TodoTaskModel

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
fun TodoEntity.toModel(): TodoTaskModel {
    return TodoTaskModel(id, title, description, priority)
}

fun TodoTaskModel.toEntity(): TodoEntity {
    return TodoEntity(id, title, description, priority)
}