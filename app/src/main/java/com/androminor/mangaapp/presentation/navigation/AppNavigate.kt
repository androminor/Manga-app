package com.androminor.mangaapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.androminor.mangaapp.presentation.signin.SignInScreen

/**
 * Created by Varun Singh
 */

    sealed class Screen(val route: String) {
        data object SignIn : Screen("signin")
        data object Manga : Screen("manga")
        data object MangaDetail:Screen("manga detail")
        data object Face:Screen("face")
    }

    @Composable
    fun AppNavigate(
        modifier:Modifier =  Modifier,
        startDestination: String = Screen.SignIn.route
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable(Screen.SignIn.route) {
                SignInScreen(
                    onNavigatingHome = {
                        navController.navigate(Screen.Manga.route) {
                            popUpTo(Screen.SignIn.route) { inclusive = true }
                        }
                    }
                )

            }

            /* val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Screen.SignIn.route) {
            composable(Screen.SignIn.route) {
                SignInScreen(
                    viewModel = signInViewModel,
                    onNavigatingHome = {
                        navController.navigate(Screen.Manga.route) {
                            popUpTo(Screen.SignIn.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Manga.route) {
                HomeScreen(onSignOut = {
                    Log.d("Navigation", "onSignOut callback triggered, navigating to SignIn")
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Manga.route) { inclusive = true }
                    }
                })
            }
        }*/
        }
    }