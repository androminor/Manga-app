package com.androminor.mangaapp.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    }

    @Composable
    fun AppNavigate() {

        val navController = rememberNavController()
        val signInViewModel: SignInViewModel = hiltViewModel()

        val currentBackStack by navController.currentBackStackEntryAsState()
        Log.d("Navigation", "Current destination: ${currentBackStack?.destination?.route}")

        LaunchedEffect(key1 = Unit) {
            val isLoggedIn = signInViewModel.checkLoginStatus()
            if (isLoggedIn) {
                navController.navigate(Screen.Manga.route)
            }
        }
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
        }
    }