package com.app.todolist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * [TodoApp] is a custom application class that extends [Application] and is annotated with [HiltAndroidApp].
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
@HiltAndroidApp
class TodoApp : Application() {
}