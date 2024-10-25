package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.details

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.ImageData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme

@ExperimentalMaterial3Api
fun NavGraphBuilder.breedDetailsScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val dataId = it.arguments?.getString("id")
        ?: throw IllegalArgumentException("id is required.")


    val breedDetailsViewModel = hiltViewModel<BreedDetailsViewModel>()
    val state by breedDetailsViewModel.state.collectAsState()

    if(state.id == null) {
        breedDetailsViewModel.setDataId(dataId)
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    else
        BreedDetailsScreen(
            state = state,
            onClose = {
                navController.navigateUp()
            },
            onWikipediaClick = {
                if (state.data != null && state.data!!.wikipediaLink != null) {
                    val intent = Intent(Intent.ACTION_VIEW, state.data!!.wikipediaLink!!.toUri())
                    val shareIntent = Intent.createChooser(intent, null)
                    it.startActivity(shareIntent)
                }
            },
            onGalleryClick = {
                 navController.navigate("breeds/gallery/${dataId}")
            },
            imageLoader = breedDetailsViewModel.imageLoader
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailsScreen(
    state: BreedDetailsState,
    onClose: () -> Unit,
    onWikipediaClick: (Context) -> Unit,
    onGalleryClick: () -> Unit,
    imageLoader: ImageLoader?
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Breed Details") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = onClose,
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onGalleryClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Gallery")
                    }
                }
            )
        },
        content = {
            if (state.error != null) {
                Text(text = state.error)
            } else if (state.data == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val scrollState = rememberScrollState()
                Column(modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(it)) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(256.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)) {
                        if (state.imageError != null) {
                            Text(text = state.imageError)
                        } else if (state.imageData == null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            SubcomposeAsyncImage(
                                model = state.imageData.url,
                                loading = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                },
                                contentDescription = "Image of a cat",
                                modifier = Modifier.fillMaxSize(),
                                imageLoader = imageLoader!!
                            )
                        }
                    }
                    Divider()
                    ListItem(headlineContent = { Text("Name") },
                        supportingContent = { Text(state.data.name) })
                    Divider()
                    ListItem(headlineContent = { Text("Alternative names") },
                        supportingContent = { Text(state.data.alternateNames.joinToString { it }) })
                    Divider()
                    ListItem(headlineContent = { Text("Description") },
                        supportingContent = { Text(state.data.description) })
                    Divider()
                    ListItem(headlineContent = { Text("Origin") },
                        supportingContent = { Text(state.data.origins.joinToString { it }) })
                    Divider()
                    ListItem(headlineContent = { Text("Temperament") },
                        supportingContent = { Text(state.data.temperament.joinToString { it }) })
                    Divider()
                    ListItem(headlineContent = { Text("Lifespan") },
                        supportingContent = { Text(state.data.lifespan + " years") })
                    Divider()
                    ListItem(headlineContent = { Text("Weight") },
                        supportingContent = { Text(state.data.weight + " kg") })
                    Divider()
                    ListItem(headlineContent = { Text("Adaptability") },
                        supportingContent = { Text(state.data.adaptability.toString() + "/5") })
                    Divider()
                    ListItem(headlineContent = { Text("Affection level") },
                        supportingContent = { Text(state.data.affectionLevel.toString() + "/5") })
                    Divider()
                    ListItem(headlineContent = { Text("Child friendly") },
                        supportingContent = { Text(state.data.childFriendly.toString() + "/5") })
                    Divider()
                    ListItem(headlineContent = { Text("Dog friendly") },
                        supportingContent = { Text(state.data.dogFriendly.toString() + "/5") })
                    Divider()
                    ListItem(headlineContent = { Text("Energy level") },
                        supportingContent = { Text(state.data.energyLevel.toString() + "/5") })
                    Divider()
                    ListItem(headlineContent = { Text("Rare") },
                        supportingContent = { Text(if (state.data.rare) "Yes" else "No") })
                    Divider()
                    if (state.data.wikipediaLink != null) {
                        ListItem(headlineContent = { Text("Wikipedia article") },
                            supportingContent = { Text("Click to open") },
                            trailingContent = { Text(state.data.wikipediaLink) },
                            modifier = Modifier.clickable { onWikipediaClick(context) })
                        Divider()
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun BreedListItemPreview() {
    CatalogTheme {
        BreedDetailsScreen(
            state = BreedDetailsState(id = "test",
            BreedData( "xd", "macka", listOf("macor", "maca"), "neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka neka macka",
            listOf("Srbija"), listOf("1", "2", "333", "4", "5", "6", "7"), "-1 do 0", "150", false, "google.com", 1, 1, 1, 1, 1),
            imageData = ImageData("xdd", "https://cdn2.thecatapi.com/ebv.jpg", "xd"), imageError = "abcd"
        ), onClose = { }, {  }, {

        }, null
        )
    }
}