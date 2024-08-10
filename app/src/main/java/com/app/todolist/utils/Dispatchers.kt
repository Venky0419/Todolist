package com.app.todolist.utils

import kotlinx.coroutines.CoroutineDispatcher

/**
 * [AppDispatchers] dispatchers for default, main and io operations of coroutines
 * @property default [CoroutineDispatcher] default dispatcher for coroutines
 * @property main [CoroutineDispatcher] main dispatcher for coroutines
 * @property io [CoroutineDispatcher] io dispatcher for coroutines
 *
 * companion object for testing purpose [createTestDispatchers]
 *
 * Created by P,Venkatesh on 07-Aug-24
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