package com.androminor.mangaapp.presentation.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.HomeMax
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.androminor.mangaapp.R
import com.androminor.mangaapp.domain.model.Manga
import com.androminor.mangaapp.presentation.manga.MangaViewModel

enum class HomeScreenTab {
    MANGA, FACE
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    mangaViewModel: MangaViewModel = hiltViewModel(),
    onSignOut: () -> Unit,
    onMangaClick: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(HomeScreenTab.MANGA) }
    val isLoggedOut by homeViewModel.isLoggedOut

    if (isLoggedOut) {
        Log.d("HomeScreen", "Detected logout state, navigating to sign in")
        onSignOut()
        homeViewModel.resetLogOut()
    }

    Scaffold(
        containerColor = Color(0xFF212121), // Set overall background to black
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF212121) // Change to black instead of surfaceVariant
            ) {
                NavigationBarItem(
                    selected = selectedTab == HomeScreenTab.MANGA,
                    onClick = { selectedTab = HomeScreenTab.MANGA },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.HomeMax,
                            contentDescription = stringResource(R.string.manga),
                            tint = if (selectedTab == HomeScreenTab.MANGA) Color.White else Color.Gray
                        )
                    },
                    label = {
                        Text(
                            stringResource(R.string.manga),
                            color = if (selectedTab == HomeScreenTab.MANGA) Color.White else Color.Gray
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent // Remove the indicator background
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == HomeScreenTab.FACE,
                    onClick = { selectedTab = HomeScreenTab.FACE },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = stringResource(R.string.face),
                            tint = if (selectedTab == HomeScreenTab.FACE) Color.White else Color.Gray
                        )
                    },
                    label = {
                        Text(
                            stringResource(R.string.face),
                            color = if (selectedTab == HomeScreenTab.FACE) Color.White else Color.Gray
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent // Remove the indicator background
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black) // Set background color to black
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                HomeScreenTab.MANGA -> MangaTabContent(mangaViewModel, onMangaClick)
                HomeScreenTab.FACE -> FaceTabContent(viewModel = homeViewModel)
            }
        }
    }
}

@Composable
fun MangaTabContent(viewModel: MangaViewModel, onMangaClick: (String) -> Unit) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val mangaItems = viewModel.mangasPaginationData.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.manga),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading && mangaItems.itemCount == 0) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null && mangaItems.itemCount == 0) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: stringResource(R.string.unknown_error),
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mangaItems.itemCount) { index ->
                    mangaItems[index]?.let { manga ->
                        MangaItem(
                            manga = manga,
                            onClick = { onMangaClick(manga.id) }
                        )
                    }
                }

                item {
                    when (val state = mangaItems.loadState.append) {
                        is LoadState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is LoadState.Error -> {
                            Text(
                                text = state.error.localizedMessage
                                    ?: stringResource(R.string.error_loading_more_data),
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun MangaItem(manga: Manga, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clickable(onClick = onClick)
    ) {
        Column {
            AsyncImage(
                model = manga.thumb,
                contentDescription = manga.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
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
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
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
