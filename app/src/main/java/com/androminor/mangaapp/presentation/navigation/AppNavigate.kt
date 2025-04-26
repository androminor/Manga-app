package com.androminor.mangaapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.androminor.mangaapp.presentation.face.FaceScreen
import com.androminor.mangaapp.presentation.home.HomeScreen
import com.androminor.mangaapp.presentation.home.HomeViewModel
import com.androminor.mangaapp.presentation.manga.MangaDetailScreen
import com.androminor.mangaapp.presentation.signin.SignInScreen
import com.androminor.mangaapp.presentation.signin.SignInViewModel
import com.androminor.mangaapp.presentation.signin.Splash

sealed class Screen(val route: String) {
    data object SignIn : Screen("signin")
    data object MangaDetail : Screen("manga_detail/{mangaId}")
    data object Face : Screen("face")
    data object Home : Screen("home")
}

@Composable
fun AppNavigate(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val signInViewModel: SignInViewModel = hiltViewModel()
    var selectedTab by remember { mutableIntStateOf(0) }
    val homeViewModel: HomeViewModel = hiltViewModel()

    val isLoggedOut by homeViewModel.isLoggedOut

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            navController.navigate(Screen.SignIn.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
            homeViewModel.resetLogOut()
        }
    }

    // Track current route for bottom navigation
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Update selected tab based on current route
    LaunchedEffect(currentRoute) {
        when {
            currentRoute == Screen.Home.route -> selectedTab = 0
            currentRoute == Screen.Face.route -> selectedTab = 1
            currentRoute?.startsWith("manga_detail") == true -> selectedTab =
                0 // Keep manga tab selected for detail screen
        }
    }

    // Splash logic
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val isLoggedIn = signInViewModel.checkLoginStatus()
        startDestination = if (isLoggedIn) Screen.Home.route else Screen.SignIn.route
    }

    // Show splash while login check
    if (startDestination == null) {
        Splash()
        return
    }

    NavHost(
        navController = navController,
        startDestination = startDestination!!,
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
            // Pass the shared homeViewModel
            HomeScreen(
                homeViewModel = homeViewModel,
                navController = navController,
                selectedTab = selectedTab,
                onTabSelected = {
                    selectedTab = it
                }
            )
        }

        composable(Screen.Face.route) {
            FaceScreen(
                selectedTab = selectedTab,
                onBackToHome = {
                    val backStackEntry = navController.previousBackStackEntry
                    // Check if we have a back stack entry from manga detail
                    if (backStackEntry?.destination?.route?.startsWith("manga_detail") == true) {
                        // Go back to the manga detail screen
                        navController.popBackStack()
                    } else {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Face.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            )

        }

        composable(
            route = Screen.MangaDetail.route,
            arguments = listOf(navArgument("mangaId") { type = NavType.StringType })
        ) { backStackEntry ->
            //Agar getString("mangaId") null ho gaya, toh return@composable kar do.
            // Yani: is composable ko yahin pe exit kar do — aage ka UI mat render karo.
            val mangaId = backStackEntry.arguments?.getString("mangaId") ?: return@composable
            MangaDetailScreen(
                navController = navController,
                mangaId = mangaId,
                selectedTab = selectedTab, // Manga tab should remain selected in detail view
                onTabSelected = {
                    selectedTab = it
                }
            )
        }
    }
}