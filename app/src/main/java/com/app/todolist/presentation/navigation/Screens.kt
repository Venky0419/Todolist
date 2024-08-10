package com.app.todolist.presentation.navigation


import androidx.navigation.NavHostController
import com.app.todolist.utils.Action
import com.app.todolist.utils.Constants.LIST_SCREEN

/**
 * [Screens] class is used to navigate between screens.
 * @param navHostController [NavHostController] is used to navigate between screens.
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
class Screens(navHostController: NavHostController) {
    val list: (Action) -> Unit = { action ->
        navHostController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }

    val task: (Int) -> Unit = { id ->
        navHostController.navigate("task/$id")
    }
}