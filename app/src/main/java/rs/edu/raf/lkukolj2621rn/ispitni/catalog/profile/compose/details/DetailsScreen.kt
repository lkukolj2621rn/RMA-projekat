package rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.compose.details

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.compose.CatalogBottomNavigationBar
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.leaderboard.LocalResultData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.profile.ProfileData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun NavGraphBuilder.profileDetailsScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val state by detailsViewModel.state.collectAsState()

    DetailsScreen(state = state) { it, i ->
        navController.navigate(it) {
            popUpTo(route) {
                inclusive = i
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(state: DetailsState, navigate: (String, Boolean) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            actions = {
                IconButton(onClick = { navigate("profile/edit", false) }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Profile")
                }
            })
        },
        bottomBar = { CatalogBottomNavigationBar("profile") { navigate(it, true) } },
    ) {
        if (state.profileData == null)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        else
            Column(Modifier.padding(it)) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(state.profileData.name, style = MaterialTheme.typography.displaySmall)
                        Text(state.profileData.nickname)
                        Text(state.profileData.email)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(
                            modifier = Modifier
                                .width(128.dp)
                            ,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("Highscore:")
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                if (state.resultsLoading)
                                    CircularProgressIndicator()
                                else
                                    Text(text = if (state.bestResult == null) "N/A" else "%.2f".format(state.bestResult), style = MaterialTheme.typography.displaySmall)
                            }
                        }
                        Column(
                            modifier = Modifier
                                .width(128.dp)
                            ,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("Global rank:")
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                if (state.leaderboardLoading)
                                    CircularProgressIndicator()
                                else
                                    Text(text = if (state.leaderboardBest == null) "N/A" else state.leaderboardBest.toString(), style = MaterialTheme.typography.displaySmall)
                            }
                        }
                    }
                }

                Divider()
                ResultsList(state.resultsError, state.resultsLoading, state.results)
            }
    }
}

@Composable
private fun ResultsList(error: String?, loading: Boolean, list: List<LocalResultData>) {
    if (error != null)
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = error)
        }
    else if (loading)
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    else
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(list) {
                ListItem(headlineContent = { Text(
                    text = ZonedDateTime.ofInstant(it.date, ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))) },
                    supportingContent = { Text("Score: " + it.result.toString()) })
                Divider()
            }
        }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    CatalogTheme {
        DetailsScreen(state = DetailsState(
            profileData = ProfileData("Ime", "username", "email@email.com"),
            bestResult = 53.550003f,
            resultsLoading = false,
            results = listOf(LocalResultData(4.45f, Instant.now()))
        )) { s: String, b: Boolean ->

        }
    }
}