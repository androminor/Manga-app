package com.androminor.mangaapp.presentation.navigation

import BottomNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.androminor.mangaapp.presentation.home.HomeScreen
import com.androminor.mangaapp.presentation.manga.MangaDetailScreen
import com.androminor.mangaapp.presentation.signin.SignInScreen
import com.androminor.mangaapp.presentation.signin.SignInViewModel

/**
 * Created by Varun Singh
 */

sealed class Screen(val route: String) {
    data object SignIn : Screen("signin")
    //data object MangaDetail : Screen("manga_detail/{mangaId}")
    data object Face : Screen("face")
    data object Home : Screen("home")

}

@Composable
fun AppNavigate(
    modifier: Modifier = Modifier,
    startDestination: String = Screen.SignIn.route
) {
    val navController = rememberNavController()
    val signInViewModel: SignInViewModel = hiltViewModel()

    LaunchedEffect(key1 = Unit) {
        val isLoggedIn = signInViewModel.checkLoginStatus()
        if (isLoggedIn) {
            navController.navigate(Screen.Home.route) {
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
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                }
            )

        }

           composable(Screen.Home.route) {
               BottomNavController(
                  navController
               )
           }


        composable("home") {
            HomeScreen(
                onSignOut = {
                    navController.navigate("signin") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onMangaClick = { mangaId ->
                    navController.navigate("mangaDetail/$mangaId")
                }
            )
        }

        composable(
              route = "mangaDetail/{mangaId}",
              arguments = listOf(navArgument("mangaId") { type = NavType.StringType })
          ) { backStackEntry ->
              val mangaId = backStackEntry.arguments?.getString("mangaId") ?: return@composable
              MangaDetailScreen(
              )
          }
    }

}