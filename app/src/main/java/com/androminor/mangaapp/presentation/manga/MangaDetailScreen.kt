package com.androminor.mangaapp.presentation.manga

import NavigationItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.HomeMax
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.androminor.mangaapp.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen(
    viewModel: MangaDetailViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val items = listOf(
        NavigationItem("Manga", Icons.Default.HomeMax, Screen.Home.route),
        NavigationItem("Face", Icons.Default.Face, Screen.Face.route)
    )
    val selectedTab = 0

    Scaffold(
        containerColor = Color(0xFF212121),  // Dark Brown
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF212121),  // Dark Brown
                contentColor = Color.White,
                modifier = Modifier.height(56.dp)
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { /* No-op since we don't navigate from detail screen */ },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (selectedTab == index) Color.White else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                color = if (selectedTab == index) Color.White else Color.Gray,
                                style = MaterialTheme.typography.h6
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF212121))  // Dark Brown for the Box
        ) {
            when (uiState) {
                is MangaDetailUiState.Loading -> {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is MangaDetailUiState.Success -> {
                    val manga = (uiState as MangaDetailUiState.Success).manga
                    val paras = manga.summary.split("\n\n")

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                            .padding(top = 32.dp)
                    ) {
                        // Top section with image on left, title to the right
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            // Left side - Manga cover image
                            AsyncImage(
                                model = manga.thumb,
                                contentDescription = manga.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(180.dp)
                                    .aspectRatio(3f / 4f)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            // Right side - Title only
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.Top)
                            ) {
                                Text(
                                    text = manga.title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.h5,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                // Subtitle (using authors as subtitle)
                                Text(
                                    text = manga.authors.joinToString(", "),
                                    fontWeight = FontWeight.Normal,
                                    style = MaterialTheme.typography.subtitle1,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    paras.forEach { paragraph ->
                        // Summary
                        Text(
                            text = paragraph,
                            fontWeight = FontWeight.Normal,
                            style = MaterialTheme.typography.body1,
                            color = Color.White,
                            lineHeight = 24.sp  // Adjust the value as needed
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                    }
                    }
                }

                is MangaDetailUiState.Error -> {
                    val message = (uiState as MangaDetailUiState.Error).message
                    Text(
                        text = message,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }

                MangaDetailUiState.Empty -> {
                    Text(
                        text = "no manga found",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}
