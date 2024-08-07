package com.app.todolist.utils

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
class AppDispatchers(
    val default: CoroutineDispatcher,
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher
) {
    companion object {
        fun createTestDispatchers(coroutineDispatcher: CoroutineDispatcher): AppDispatchers {
            return AppDispatchers(
                default = coroutineDispatcher,
                main = coroutineDispatcher,
                io = coroutineDispatcher
            )
        }
    }
}