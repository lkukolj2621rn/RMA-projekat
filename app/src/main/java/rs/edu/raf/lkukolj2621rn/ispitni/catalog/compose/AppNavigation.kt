package rs.edu.raf.lkukolj2621rn.ispitni.catalog.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.details.breedDetailsScreen
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.gallery.breedGalleryScreen
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.imageviewer.breedImageViewerScreen
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.list.breedListScreen
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.quiz.quizScreen
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.quiz.quizStartScreen
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.search.breedSearchScreen
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.compose.leaderboardScreen
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.compose.details.profileDetailsScreen
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.compose.register.profileDataScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun AppNavigation() {

    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(
            navController = navController,
            startDestination = "profile/register",
        ) {
            breedListScreen(route = "breeds", navController = navController)
            breedSearchScreen(route = "breeds/search", navController = navController)
            breedDetailsScreen(route = "breeds/{id}", navController = navController)
            breedGalleryScreen(route = "breeds/gallery/{id}", navController = navController)
            breedImageViewerScreen(route = "breeds/image/{id}/{index}", navController = navController)
            profileDetailsScreen(route = "profile", navController = navController)
            profileDataScreen(route = "profile/register", navController = navController, true)
            profileDataScreen(route = "profile/edit", navController = navController, false)
            quizStartScreen(route = "quiz/start", navController = navController)
            quizScreen(route = "quiz", navController = navController)
            leaderboardScreen(route = "leaderboard", navController = navController)
        }
    }
}
