import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.androminor.mangaapp.R
import com.androminor.mangaapp.presentation.navigation.Screen

/**
 * Created by Varun Singh
 *
 */@Composable
fun BottomNavController(
    navController: NavController,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    mangaId: String? = null // Add parameter to track current manga ID
) {
    val items = listOf(
        NavigationItem(
            title = "Manga",
            icon = R.drawable.baseline_menu_book_24,
            route = Screen.Home.route
        ),
        NavigationItem(
            title = "Face",
            icon = null,
            route = Screen.Face.route
        )
    )
    val iconContainerWidth = 60.dp
    val iconContainerHeight = 36.dp
    val fixedIconSize = 20.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF212121))
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Manga Tab - Modified with the new layout
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                // Icon with background
                Box(
                    modifier = Modifier
                        .width(iconContainerWidth)
                        .height(iconContainerHeight)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF333333))
                        .clickable {
                            onTabSelected(0)
                            // Don't navigate if we're already on manga detail screen
                            if (mangaId != null && navController.currentDestination?.route?.startsWith("manga_detail") == true) {
                                return@clickable
                            }
                            navController.navigate(items[0].route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_book_24),
                        contentDescription = "Manga",
                        // tint = if (selectedTab == 0) Color.White else Color.Gray,
                        tint = Color.White,
                        modifier = Modifier.size(fixedIconSize)
                    )
                }

                // Small space between icon and text
                Spacer(modifier = Modifier.height(4.dp))

                // Text below the icon
                Text(
                    text = "Manga",
                    color = if (selectedTab == 0) Color.White else Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // Face Tab
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (selectedTab == 1) Color.White else Color.White)
                    .clickable {
                        onTabSelected(1)
                        navController.navigate(items[1].route) {
                            launchSingleTop = true
                        }
                    }
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Face",
                    color = if (selectedTab == 1) Color.Black else Color.Gray,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

data class NavigationItem(
    val title: String,
    val icon: Int? = null,
    val route: String
)