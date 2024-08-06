package com.app.todolist.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.todolist.presentation.view.composables.TodoListComposable
import com.app.todolist.presentation.view.composables.TodoTaskComposable
import com.app.todolist.utils.Constants.LIST_ARG_KEY
import com.app.todolist.utils.Constants.LIST_SCREEN
import com.app.todolist.utils.Constants.TASK_ARG_KEY
import com.app.todolist.utils.Constants.TASK_SCREEN

/**
 * Created by P,Venkatesh on 06-Aug-24
 *
 */
@Composable
fun ConfigureNavigation(navHostController: NavHostController) {
    val screen = remember(navHostController) {
        Screens(navHostController = navHostController)
    }

    NavHost(navController = navHostController, startDestination = LIST_SCREEN) {
        composable(
            LIST_SCREEN, arguments = listOf(navArgument(LIST_ARG_KEY) {
                type = NavType.StringType
            }),
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
        ) {
            TodoListComposable {
                screen.task(it)
            }
        }

        composable(
            TASK_SCREEN, arguments = listOf(navArgument(TASK_ARG_KEY) {
                type = NavType.StringType
            }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            TodoTaskComposable {
                screen.list(it)
            }
        }
    }
}