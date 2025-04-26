    package com.androminor.mangaapp.presentation.manga

    import BottomNavController
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
import androidx.navigation.NavController
import coil.compose.AsyncImage

    @Composable
    fun MangaDetailScreen(
        viewModel: MangaDetailViewModel = hiltViewModel(),
        navController: NavController,
        mangaId: String,
        selectedTab: Int,
        onTabSelected: (Int) -> Unit
    ) {


        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Scaffold(
            containerColor = Color(0xFF212121),
     /*       topBar = {
                TopAppBar(
                    title = { *//* Empty title *//* },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF212121),
                        titleContentColor = Color.White,
                    ),
                    navigationIcon = {
                        // Add back button if needed
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        // Star icon in the top right corner
                        IconButton(onClick = { *//* Favorite action *//* }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favorite",
                                tint = Color.White
                            )
                        }
                    }
                )
            },*/
            bottomBar = {
                BottomNavController(
                    navController = navController,
                    selectedTab = selectedTab,
                    onTabSelected = {tab ->
                    if(tab==0){
                        // Do nothing when clicking on Manga tab since we're already in a Manga screen
                        // This prevents going back to HomeScreen
                        onTabSelected(0)


                    }
                    else onTabSelected(tab)}
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFF212121))
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
                                    lineHeight = 24.sp
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
                            text = "No manga found",
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