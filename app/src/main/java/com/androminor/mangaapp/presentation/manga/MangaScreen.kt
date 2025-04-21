package com.androminor.mangaapp.presentation.manga

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.androminor.mangaapp.R
import com.androminor.mangaapp.domain.model.Manga

@Composable
fun MangaScreen(
    viewModel: MangaViewModel = hiltViewModel(),
    onMangaClick: (String) -> Unit,
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val mangaItems = viewModel.mangasPaginationData.collectAsLazyPagingItems()
    val gridState = rememberLazyGridState()
Box( modifier = Modifier
    .fillMaxSize()
    .background(Color(0xFF252525))){

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Initial loading state
        if (isLoading && mangaItems.itemCount == 0) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        // Error state when no items are loaded
        else if (errorMessage != null && mangaItems.itemCount == 0) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: stringResource(R.string.unknown_error),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        // Content loaded
        else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = gridState,
                contentPadding = PaddingValues(8.dp)
            ) {
                items(mangaItems.itemCount) { index ->
                    mangaItems[index]?.let { manga ->
                        MangaItem(
                            manga = manga,
                            onClick = { onMangaClick(manga.id) }
                        )
                    }
                }

                when (val appendState = mangaItems.loadState.append) {
                    is LoadState.Loading -> {
                        item(span = { GridItemSpan(3) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    }
                    is LoadState.Error -> {
                        item(span = { GridItemSpan(3) }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = appendState.error.localizedMessage
                                        ?: stringResource(R.string.error_loading_more_data),
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(onClick = { mangaItems.retry() }) {
                                    Text(text = "Retry")
                                }
                            }
                        }
                    }
                    is LoadState.NotLoading -> {
                        if (appendState.endOfPaginationReached && mangaItems.itemCount > 0) {
                            item(span = { GridItemSpan(3) }) {
                                Text(
                                    text = "No more manga to load",
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Show overlay loading indicator for refresh operations
            if (mangaItems.loadState.refresh is LoadState.Loading && mangaItems.itemCount > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

}

@Composable
fun MangaItem(
    manga: Manga,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick)
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(manga.thumb)
                    .crossfade(true)
                    .placeholder(ColorDrawable(Color.Gray.toArgb()))
                    .error(ColorDrawable(Color.Transparent.copy(alpha = 0.5f).toArgb()))
                    .build(),
                contentDescription = manga.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(2f / 3f)
                    .fillMaxWidth()
            )


        }
    }
}