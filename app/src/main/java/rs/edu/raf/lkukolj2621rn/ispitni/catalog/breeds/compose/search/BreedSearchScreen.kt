package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.list.BreedList
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme

fun NavGraphBuilder.breedSearchScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val breedSearchViewModel = hiltViewModel<BreedSearchViewModel>()
    val state by breedSearchViewModel.state.collectAsState()

    BreedSearch(
        state = state,
        onItemClick = {
            navController.navigate(route = "breeds/${it.id}")
        },
        onQueryChange = {
            breedSearchViewModel.setSearch(it)
        },
        onSearch = {
            breedSearchViewModel.setSearch(it)
        },
        onActiveChange = {
            if (!it)
                navController.navigateUp()
        },
        onBack = {
            navController.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedSearch(state: BreedSearchState, onItemClick: (BreedData) -> Unit, onActiveChange: (Boolean) -> Unit,
                onSearch: (String) -> Unit, onQueryChange: (String) -> Unit, onBack: () -> Unit) {
    SearchBar(query = state.query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = true,
        onActiveChange = onActiveChange,
        leadingIcon = { IconButton(onClick = onBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back")
        }}) {
        if (state.error != null) {
            Text(state.error)
        } else if (state.searching) {
            Box(
                Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            BreedList(paddingValues = PaddingValues(), items = state.items, onItemClick = onItemClick)
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview
@Composable
fun PreviewBreedSearchScreen() {
    val state = remember {
        MutableStateFlow(BreedSearchState())
    }
    val data = listOf(
        BreedData("xd", "macka", listOf("macor"), "neka macka", listOf("Srbija"), listOf("1", "2", "3", "4", "5", "6", "7"), "-1 do 0", "150", false, "google.com", 1, 1, 1, 1, 1),
        BreedData("xdd", "lav", listOf("macor"), "neka macka", listOf("Srbija"), listOf("1", "2", "3", "4", "5", "6", "7"), "-1 do 0", "150", false, "google.com", 1, 1, 1, 1, 1),
        BreedData("smesno", "lav pivo", listOf("macor"), "neka macka", listOf("Srbija"), listOf("1", "2", "3", "4", "5", "6", "7"), "-1 do 0", "150", false, "google.com", 1, 1, 1, 1, 1),
        BreedData("haha", "spajdrmen", listOf("macor"), "neka macka", listOf("Srbija"), listOf("1", "2", "3", "4", "5", "6", "7"), "-1 do 0", "150", false, "google.com", 1, 1, 1, 1, 1)
    )
    CatalogTheme {
        key(state.value) {
            BreedSearch(
                state = state.value,
                onItemClick = {},
                onActiveChange = {},
                onBack = {},
                onQueryChange = {
                    state.getAndUpdate { s -> s.copy(query = it) }
                },
                onSearch = {
                    state.getAndUpdate { s ->
                        s.copy(items = data.filter { b -> b.name.contains(it, true) })
                    }
                }
            )
        }
    }
}