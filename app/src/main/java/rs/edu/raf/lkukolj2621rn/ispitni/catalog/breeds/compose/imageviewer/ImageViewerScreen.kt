package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.imageviewer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.gallery.GalleryState
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.gallery.GalleryViewModel
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.ui.theme.CatalogTheme
import kotlin.math.max

@ExperimentalMaterial3Api
fun NavGraphBuilder.breedImageViewerScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val dataId = it.arguments?.getString("id")
        ?: throw IllegalArgumentException("id is required.")

    val dataIndex = it.arguments?.getString("index")?.toInt()
        ?: throw IllegalArgumentException("index is required.")


    val galleryViewModel = hiltViewModel<GalleryViewModel>()
    val state by galleryViewModel.state.collectAsState()

    if(state.id == null) {
        galleryViewModel.setDataId(dataId)
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    else
        ImageViewerScreen(
            state = state,
            onClose = {
                navController.navigateUp()
            },
            initialPage = dataIndex,
            imageLoader = galleryViewModel.imageLoader
        )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ImageViewerScreen(
    state: GalleryState,
    onClose: () -> Unit,
    initialPage: Int,
    imageLoader: ImageLoader?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {  },
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
            if (state.error != null)
                Box(
                    modifier = Modifier.fillMaxSize().padding(it),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(state.error)
                }
            else {
                val pagerState = rememberPagerState(initialPage+1) {
                    max(initialPage+1, state.list.count())
                }
                HorizontalPager(state = pagerState,
                    modifier = Modifier.fillMaxSize().padding(it)) {
                    if (it >= state.list.count())
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    else
                        SubcomposeAsyncImage(
                            model = state.list[it].url,
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
        }
    )
}