package com.app.todolist.data.mappers

import com.app.todolist.data.entities.TodoEntity
import com.app.todolist.domain.models.TodoTaskModel

/**
 * [TodoEntity.toModel] is an extension function that converts a TodoEntity object to a
 * TodoTaskModel object and @return TodoTaskModel object.
 * [TodoTaskModel.toEntity] is an extension function that converts a
 * TodoTaskModel object to a TodoEntity object and @return TodoEntity object.
 *
 * Created by P,Venkatesh on 07-Aug-24
 */
fun TodoEntity.toModel(): TodoTaskModel {
    return TodoTaskModel(id, title, description, priority)
}

fun TodoTaskModel.toEntity(): TodoEntity {
    return TodoEntity(id, title, description, priority)
}