package com.app.todolist.utils

import android.content.Context

/**
 * [UiText] sealed class to handle string resources and dynamic strings in the app.
 * @see [DynamicString] for dynamic strings.
 * @see [StringResource] for string resources.
 * [asString] method to convert the [UiText] to a string by providing a [Context] as a parameter.
 * @return String representation of the [UiText].
 *
 * Created by P,Venkatesh on 07-Aug-24
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