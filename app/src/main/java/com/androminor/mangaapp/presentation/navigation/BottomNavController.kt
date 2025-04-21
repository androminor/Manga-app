import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.HomeMax
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.androminor.mangaapp.presentation.face.FaceScreen
import com.androminor.mangaapp.presentation.home.HomeScreen
import com.androminor.mangaapp.presentation.navigation.Screen

/**
 * Created by Varun Singh
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavController(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavigationItem("Manga", Icons.Default.HomeMax, Screen.Home.route),
        NavigationItem("Face", Icons.Default.Face, Screen.Face.route)
    )
    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black,
                contentColor = Color.White,
                modifier = Modifier.height(56.dp)
            )
            {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
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
                                style = MaterialTheme.typography.labelSmall
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    onSignOut = {
                        navController.navigate("signin") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onMangaClick = { mangaId ->
                        navController.navigate("manga_detail/$mangaId")
                    }
                )

                1 -> FaceScreen(onSignOut = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                })
            }
        }

    }

}


data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

