package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.compose.CatalogBottomNavigationBar
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme

fun NavGraphBuilder.breedListScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val breedListViewModel = hiltViewModel<BreedListViewModel>()
    val state by breedListViewModel.state.collectAsState()

    BreedListScreen(
        state = state,
        navigate = { path: String, pop: Boolean ->
            navController.navigate(path) {
                if (pop)
                    popUpTo("breeds") {
                        inclusive = true
                    }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedListScreen(state: BreedListState, navigate: (String, Boolean) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Breed List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                actions = {
                    IconButton(onClick = { navigate("breeds/search", false) }) {
                        Icon(imageVector = Icons.Default.Search,
                            contentDescription = "Search breeds")
                    }
                }
            )
        },
        content = {
            if (state.list.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    contentAlignment = Alignment.Center,
                ) {
                    if (state.error != null) {
                        Text(state.error)
                    } else {
                        CircularProgressIndicator()
                    }
                }
            } else {
                BreedList(
                    paddingValues = it,
                    items = state.list,
                    onItemClick = { navigate("breeds/${it.id}", false) },
                )
            }
        },
        bottomBar = {
            CatalogBottomNavigationBar("breeds") { navigate(it, true) }
        }
    )
}

@Composable
fun BreedList(paddingValues: PaddingValues, items: List<BreedData>, onItemClick: (BreedData) -> Unit) {
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),) {
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            items(items = items, key = {it.id}) {
                BreedListItem(
                    data = it,
                    onClick = {
                        onItemClick(it)
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewBreedListScreen() {
    CatalogTheme {
        BreedListScreen(
            state = BreedListState(listOf(BreedData("xd", "macka", listOf("macor"), "neka macka", listOf("Srbija"), listOf("1", "2", "3", "4", "5", "6", "7"), "-1 do 0", "150", false, "google.com", 1, 1, 1, 1, 1))),
            navigate = { _: String, _: Boolean -> }
        )
    }
}