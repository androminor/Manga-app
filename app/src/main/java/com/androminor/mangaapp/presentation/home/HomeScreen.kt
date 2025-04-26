package com.androminor.mangaapp.presentation.home

import BottomNavController
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.androminor.mangaapp.R
import com.androminor.mangaapp.domain.model.Manga
import com.androminor.mangaapp.presentation.manga.MangaViewModel
import com.androminor.mangaapp.presentation.navigation.Screen

@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    mangaViewModel: MangaViewModel = hiltViewModel(),
    navController: NavController,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val isLoggedOut by homeViewModel.isLoggedOut

    if (isLoggedOut) {
        Log.d("HomeScreen", "Detected logout state, navigating to sign in")
        navController.navigate(Screen.SignIn.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
        homeViewModel.resetLogOut()
    }

    Scaffold(
        containerColor = Color(0xFF212121),
        bottomBar = {
            BottomNavController(
                navController = navController,
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {
            MangaTabContent(
                viewModel = mangaViewModel,
                onMangaClick = { mangaId ->
                    navController.navigate("manga_detail/$mangaId")
                }
            )
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
                items(
                    count = mangaItems.itemCount,
                    key = { index -> mangaItems[index]?.id ?: index }
                ) { index ->
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
