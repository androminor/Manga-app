package com.androminor.mangaapp.presentation.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.HomeMax
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androminor.mangaapp.R

enum class HomeScreenTab {
    MANGA, FACE
}
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onSignOut: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(HomeScreenTab.MANGA) }
    val isLoggedOut by viewModel.isLoggedOut

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            Log.d("HomeScreen", "Detected logout state, navigating to sign in")
            onSignOut()
            viewModel.resetLogOut()
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.Black) {
                NavigationBarItem(
                    selected = selectedTab == HomeScreenTab.MANGA,
                    onClick = { selectedTab = HomeScreenTab.MANGA },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.HomeMax,
                            contentDescription = stringResource(R.string.manga)
                        )
                    },
                    label = { Text(stringResource(R.string.manga)) }
                )

                NavigationBarItem(
                    selected = selectedTab == HomeScreenTab.FACE,
                    onClick = { selectedTab = HomeScreenTab.FACE },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = stringResource(R.string.face)
                        )
                    },
                    label = { Text(stringResource(R.string.face)) }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                HomeScreenTab.MANGA -> MangaTabContent()
                HomeScreenTab.FACE -> {
                    FaceTabContent(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MangaTabContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.manga),
            fontSize = 24.sp
        )

    }
}

@Composable
fun FaceTabContent(viewModel: HomeViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.welcome_to_zenithra),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(60.dp))
            Button(
                onClick = {
                    Log.d("FaceTabContent", "Sign out button clicked")
                    viewModel.signOut()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red.copy(alpha = 0.7f)
                )
            ) {
                Text(
                    stringResource(R.string.sign_out),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}