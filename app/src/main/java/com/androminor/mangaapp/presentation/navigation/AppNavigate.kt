package com.androminor.mangaapp.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.androminor.mangaapp.presentation.home.HomeScreen
import com.androminor.mangaapp.presentation.signin.SignInScreen
import com.androminor.mangaapp.presentation.signin.SignInViewModel

/**
 * Created by Varun Singh
 */

    sealed class Screen(val route: String) {
        data object SignIn : Screen("signin")
        data object Manga : Screen("manga")
        data object MangaDetail:Screen("manga_detail")
        data object Face:Screen("face")
    }

    @Composable
    fun AppNavigate(
        modifier:Modifier =  Modifier,
        startDestination: String = Screen.SignIn.route
    ) {
        val navController = rememberNavController()
        val signInViewModel: SignInViewModel = hiltViewModel()

        LaunchedEffect(key1 = Unit) {
            val isLoggedIn = signInViewModel.checkLoginStatus()
            if (isLoggedIn) {
                navController.navigate(Screen.Manga.route) {
                    popUpTo(Screen.SignIn.route) { inclusive = true }
                }
            }
        }
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
            composable(Screen.Manga.route){

            }

            composable(Screen.Manga.route) {
                HomeScreen(onSignOut = {
                    Log.d("Navigation", "onSignOut callback triggered, navigating to SignIn")
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Manga.route) { inclusive = true }
                    }
                })
            }

        }
    }