package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.gallery

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.layout.ContentScale
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
fun NavGraphBuilder.breedGalleryScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val dataId = it.arguments?.getString("id")
        ?: throw IllegalArgumentException("id is required.")


    val galleryViewModel = hiltViewModel<GalleryViewModel>()
    val state by galleryViewModel.state.collectAsState()

    if(state.id == null) {
        galleryViewModel.setDataId(dataId)
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    else
        GalleryScreen(
            state = state,
            onClose = {
                navController.navigateUp()
            },
            onClick = {
                navController.navigate("breeds/image/$dataId/$it")
            },
            imageLoader = galleryViewModel.imageLoader
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    state: GalleryState,
    onClose: () -> Unit,
    onClick: (Int) -> Unit,
    imageLoader: ImageLoader?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Breed Gallery") },
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
                }
            )
        },
        content = {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                LazyVerticalGrid(columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                    items(state.list) {
                        Box(
                            modifier = Modifier.fillMaxSize().aspectRatio(1f)
                                .clickable { onClick(state.list.indexOf(it)) }
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            SubcomposeAsyncImage(
                                model = it.url,
                                loading = {
                                    CircularProgressIndicator()
                                },
                                contentDescription = "Image of a cat",
                                contentScale = ContentScale.FillWidth,
                                imageLoader = imageLoader!!
                            )
                        }
                    }
                }
                if (state.error != null)
                    Text(state.error)
                if (state.loading)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
            }
        }
    )
}

@Preview
@Composable
fun GalleryPreview() {
    CatalogTheme {
        GalleryScreen(
            state = GalleryState(id = "test",
            /*list = listOf(ImageData("xdd", "https://cdn2.thecatapi.com/ebv.jpg", "xd"),
                    ImageData("xdd1", "https://cdn2.thecatapi.com/ebv.jpg", "xd"),
                    ImageData("xdd2", "https://cdn2.thecatapi.com/ebv.jpg", "xd"),
                    ImageData("xdd3", "https://cdn2.thecatapi.com/ebv.jpg", "xd"),
                    ImageData("xdd4", "https://cdn2.thecatapi.com/ebv.jpg", "xd")
            )*/
        ), {
        }, {}, null
        )
    }
}