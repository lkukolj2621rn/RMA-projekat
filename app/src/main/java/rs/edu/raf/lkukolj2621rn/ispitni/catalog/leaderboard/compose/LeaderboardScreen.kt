package rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.compose.CatalogBottomNavigationBar
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.ResultData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme

fun NavGraphBuilder.leaderboardScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val leaderboardViewModel = hiltViewModel<LeaderboardViewModel>()
    val state by leaderboardViewModel.state.collectAsState()

    LeaderboardScreen(
        state = state,
        navigate = {
            navController.navigate(it) {
                popUpTo("leaderboard") {
                    inclusive = true
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(state: LeaderboardState, navigate: (String) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Leaderboard") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ))
        },
        bottomBar = { CatalogBottomNavigationBar("leaderboard", navigate) }
    ) {
        if (state.error != null)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = state.error)
            }
        else if (state.leaderboard == null)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        else
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                items(state.leaderboard) {
                    ListItem(headlineContent = { Text(text = it.nickname + " (" + it.quizNumber.toString() + " games played)") },
                        supportingContent = { Text(text = "Score: " + it.result.toString())},
                        leadingContent = { Text(text = "#" + it.ranking.toString())})
                    Divider()
                }
            }
    }
}

@Preview
@Composable
fun LeaderboardScreenPreview() {
    CatalogTheme {
        LeaderboardScreen(state = LeaderboardState(listOf(ResultData("abcd", 4.45f, 2, 1),
            ResultData("abcd", 4.45f, 2, 2)))) {
            
        }
    }
}