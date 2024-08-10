package com.app.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.app.todolist.presentation.navigation.ConfigureNavigation
import com.app.todolist.ui.theme.TodolistTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * [MainActivity] is the entry point of the application.
 * It sets up the main UI content and navigation. It also handles the splash screen.
 * It extends [ComponentActivity]. It uses Hilt for dependency injection.
 *
 * Created by P,Venkatesh on 06-Aug-24
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            TodolistTheme {
                val navController = rememberNavController()
                ConfigureNavigation(navHostController = navController)
            }
        }
    }
}