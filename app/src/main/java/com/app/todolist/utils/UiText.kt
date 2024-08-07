package com.app.todolist.utils

import android.content.Context

/**
 * Created by P,Venkatesh on 07-Aug-24
 *
 */
sealed class UiText {

    data class DynamicString(val text: String) : UiText()
    data class StringResource(val resId: Int) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> context.getString(resId)
        }
    }
}