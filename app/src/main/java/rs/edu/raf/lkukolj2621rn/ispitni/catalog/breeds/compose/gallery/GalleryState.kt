package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.compose.gallery

import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.ImageData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.BreedData

data class GalleryState(
    val id: String? = null,
    val list: List<ImageData> = listOf(),
    val loading: Boolean = true,
    val error: String? = null,
)
