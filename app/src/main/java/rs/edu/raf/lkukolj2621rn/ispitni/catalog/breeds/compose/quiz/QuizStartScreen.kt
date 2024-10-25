package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.list.BreedListScreen
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.list.BreedListViewModel
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.compose.CatalogBottomNavigationBar
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme

fun NavGraphBuilder.quizStartScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    QuizStartScreen(
        navigate = { path: String, pop: Boolean ->
            navController.navigate(path) {
                if (pop)
                    popUpTo("quiz/start") {
                        inclusive = true
                    }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizStartScreen(navigate: (String, Boolean) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Quiz") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ))},
        bottomBar = { CatalogBottomNavigationBar("quiz/start") { navigate(it, true) } }
    ) {
        Column(modifier = Modifier.padding(it).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Tap the button below to begin a cat fact quiz.")
            Button(onClick = { navigate("quiz", false) }) {
                Text(text = "Begin quiz")
            }
        }
    }
}

@Preview
@Composable
fun QuizStartScreenPreview() {
    CatalogTheme {
        QuizStartScreen { _: String, _: Boolean ->

        }
    }
}