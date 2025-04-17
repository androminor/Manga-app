package com.androminor.mangaapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

/**
 * Created by Varun Singh
 */

sealed class Screen(val route:String){
    data object SignIn:Screen("signin")
    data object Home: Screen("home")
}

@Composable
fun AppNavigate() {

val navController = rememberNavController()
}